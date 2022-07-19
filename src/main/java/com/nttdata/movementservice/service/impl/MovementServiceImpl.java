package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.dto.BankAccountDto;
import com.nttdata.movementservice.dto.MergeBankAccountsDto;
import com.nttdata.movementservice.dto.MovementDto;
import com.nttdata.movementservice.dto.request.*;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.exception.MovementNotFoundException;
import com.nttdata.movementservice.model.Movement;
import com.nttdata.movementservice.repo.IMovementRepo;
import com.nttdata.movementservice.service.IBankAccountService;
import com.nttdata.movementservice.service.ICreditService;
import com.nttdata.movementservice.service.IMovementService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements IMovementService {

    private final IMovementRepo repo;
    private final ICreditService creditService;
    private final IBankAccountService bankAccountService;

    @Override
    public Flux<MovementDto> getAll() {
        return repo.findAll().map(MovementMapper::toMovementDto);
    }

    @Override
    public Mono<MovementDto> getById(String id) {
        return repo.findById(id)
                .map(MovementMapper::toMovementDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, id))));
    }

    @Override
    public Flux<MovementDto> getByBankAccountId(String bankAccountId) {
        return repo.findByTargetBankAccountId(bankAccountId)
                .map(MovementMapper::toMovementDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, bankAccountId))));
    }

    @Override
    public Flux<MovementDto> getByCreditId(String creditId) {
        return repo.findByCreditId(creditId)
                .map(MovementMapper::toMovementDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, creditId))));
    }

    @Override
    public Mono<MovementDto> registerDeposit(TransactionBankAccountDto transactionBankAccountDto) {
        return Mono.zip(bankAccountService.getBankAccountById(transactionBankAccountDto.getBankAccountId()),
                        repo.findByTypeStartsWithAndTargetBankAccountId(Constants.TRANSACTION, transactionBankAccountDto.getBankAccountId()).collectList())
                .flatMap(tuple2 -> validateDepositFee(tuple2, transactionBankAccountDto.getAmount()));
    }

    @Override
    public Mono<MovementDto> registerWithdrawal(TransactionBankAccountDto transactionBankAccountDto) {
        return Mono.zip(bankAccountService.getBankAccountById(transactionBankAccountDto.getBankAccountId()),
                        repo.findByTypeStartsWithAndTargetBankAccountId(Constants.TRANSACTION, transactionBankAccountDto.getBankAccountId()).collectList())
                .flatMap(tuple2 -> validateWithdrawalFee(tuple2, transactionBankAccountDto.getAmount()));
    }

    @Override
    public Mono<MovementDto> registerTransferToOwnBankAccount(TransferBankAccountDto transferBankAccountDto) {
        return Mono.zip(bankAccountService.getBankAccountById(transferBankAccountDto.getSourceBankAccountId()),
                        bankAccountService.getBankAccountById(transferBankAccountDto.getTargetBankAccountId()))
                .map(bankAccounts -> validateOwnTransfer(bankAccounts.getT1(), bankAccounts.getT2(), transferBankAccountDto.getAmount()))
                .flatMap(mergeBankAccounts -> Mono.zip(
                        bankAccountService.updateBankAccountById(mergeBankAccounts.getBankAccount1().getId(), mergeBankAccounts.getBankAccount1()),
                        bankAccountService.updateBankAccountById(mergeBankAccounts.getBankAccount2().getId(), mergeBankAccounts.getBankAccount2()),
                        repo.save(MovementMapper.toMovement(transferBankAccountDto).toBuilder().timestamp(LocalDateTime.now()).type(Constants.TRANSFER_TO_OWN_BANK_ACCOUNTS).build())))
                .map(transaction -> MovementMapper.toMovementDto(transaction.getT3()));
    }

    @Override
    public Mono<MovementDto> registerTransferToThirdPartiesBankAccount(TransferBankAccountDto transferBankAccountDto) {
        return Mono.zip(bankAccountService.getBankAccountById(transferBankAccountDto.getSourceBankAccountId()),
                        bankAccountService.getBankAccountById(transferBankAccountDto.getTargetBankAccountId()))
                .map(bankAccounts -> validateTransferToThirdParties(bankAccounts.getT1(), bankAccounts.getT2(), transferBankAccountDto.getAmount()))
                .flatMap(mergeBankAccounts -> Mono.zip(
                        bankAccountService.updateBankAccountById(mergeBankAccounts.getBankAccount1().getId(), mergeBankAccounts.getBankAccount1()),
                        bankAccountService.updateBankAccountById(mergeBankAccounts.getBankAccount2().getId(), mergeBankAccounts.getBankAccount2()),
                        repo.save(MovementMapper.toMovement(transferBankAccountDto).toBuilder()
                                .timestamp(LocalDateTime.now())
                                .type(Constants.TRANSFER_TO_THIRD_PARTIES_BANK_ACCOUNTS)
                                .build())))
                .map(transaction -> MovementMapper.toMovementDto(transaction.getT3()));
    }

    @Override
    public Mono<MovementDto> registerCreditPayment(MovementCreditDto movementCreditDto) {
        return creditService.getById(movementCreditDto.getCreditId())
                .filter(creditDto -> Constants.PERSONAL_CREDIT.equals(creditDto.getType()) || Constants.BUSINESS_CREDIT.equals(creditDto.getType()))
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, String.format(Constants.INVALID_CREDIT_TYPE))))
                .filter(creditDto -> creditDto.getAmountPaid() + movementCreditDto.getAmount() <= creditDto.getAmountToPay())
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, "Credit amount paid is greater than credit amount to pay")))
                .map(creditDto -> creditDto.toBuilder().amountPaid(creditDto.getAmountPaid() + movementCreditDto.getAmount()).build())
                .flatMap(creditDto -> creditService.updateById(creditDto.getId(), creditDto))
                .map(creditDto -> MovementMapper.toMovement(movementCreditDto))
                .map(movement -> movement.toBuilder().timestamp(LocalDateTime.now()).type(Constants.CREDIT_PAYMENT).build())
                .flatMap(repo::save)
                .map(MovementMapper::toMovementDto);
    }

    @Override
    public Mono<MovementDto> registerCreditCardPayment(MovementCreditDto movementCreditDto) {
        return creditService.getById(movementCreditDto.getCreditId())
                .filter(creditDto -> Constants.PERSONAL_CREDIT_CARD.equals(creditDto.getType()) || Constants.BUSINESS_CREDIT_CARD.equals(creditDto.getType()))
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, String.format(Constants.INVALID_CREDIT_TYPE))))
                .filter(creditDto -> creditDto.getBalance() + movementCreditDto.getAmount() <= creditDto.getCreditLine())
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.BALANCE_IS_GREATER_THAN_CREDIT_LINE)))
                .map(creditDto -> creditDto.toBuilder().balance(creditDto.getBalance() + movementCreditDto.getAmount()).build())
                .flatMap(creditDto -> creditService.updateById(creditDto.getId(), creditDto))
                .map(creditDto -> MovementMapper.toMovement(movementCreditDto))
                .map(movement -> movement.toBuilder().timestamp(LocalDateTime.now()).type(Constants.CREDIT_CARD_PAYMENT).build())
                .flatMap(repo::save)
                .map(MovementMapper::toMovementDto);
    }

    @Override
    public Mono<MovementDto> registerCreditCardConsumption(MovementCreditDto consumptionCreditDto) {
        return creditService.getById(consumptionCreditDto.getCreditId())
                .filter(creditDto -> Constants.PERSONAL_CREDIT_CARD.equals(creditDto.getType()) || Constants.BUSINESS_CREDIT_CARD.equals(creditDto.getType()))
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, String.format(Constants.INVALID_CREDIT_TYPE))))
                .filter(creditDto -> creditDto.getBalance() >= consumptionCreditDto.getAmount())
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.CONSUMPTION_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .map(creditDto -> creditDto.toBuilder().balance(creditDto.getBalance() - consumptionCreditDto.getAmount()).build())
                .flatMap(creditDto -> creditService.updateById(creditDto.getId(), creditDto))
                .map(creditDto -> MovementMapper.toMovement(consumptionCreditDto))
                .map(movement -> movement.toBuilder().timestamp(LocalDateTime.now()).type(Constants.CREDIT_CARD_CONSUMPTION).build())
                .flatMap(repo::save)
                .map(MovementMapper::toMovementDto);
    }

    @Override
    public Mono<MovementDto> registerDebitCardPayment(MovementDebitCardDto movementDebitCardDto) {
        return registerDebitCardMovement(movementDebitCardDto, Constants.DEBIT_CARD_PAYMENT);
    }

    @Override
    public Mono<MovementDto> registerDebitCardWithdrawal(MovementDebitCardDto movementDebitCardDto) {
        return registerDebitCardMovement(movementDebitCardDto, Constants.DEBIT_CARD_WITHDRAWAL);
    }

    private Mono<MovementDto> registerWithMainAccount(BankAccountDto mainAccountId, Double paymentAmount, String type) {
        return Mono.just(mainAccountId.toBuilder().balance(mainAccountId.getBalance() - paymentAmount).build())
                .flatMap(modifiedAccount -> Mono.zip(bankAccountService.updateBankAccountById(modifiedAccount.getId(), modifiedAccount),
                        repo.save(Movement.builder()
                                .amount(paymentAmount)
                                .targetBankAccountId(modifiedAccount.getId())
                                .timestamp(LocalDateTime.now())
                                .type(type)
                                .build())))
                .map(tuple -> MovementMapper.toMovementDto(tuple.getT2()));
    }

    private Mono<MovementDto> registerWithSecondaryAccount(List<String> secondaryAccountIds, Double paymentAmount, String type) {
        return Flux.fromIterable(secondaryAccountIds)
                .flatMap(bankAccountService::getBankAccountById)
                .filter(secondaryAccount -> secondaryAccount.getBalance() >= paymentAmount)
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, "Saldo insuficiente en las cuentas asociadas")))
                .next()
                .map(account -> account.toBuilder().balance(account.getBalance() - paymentAmount).build())
                .flatMap(modifiedAccount -> Mono.zip(bankAccountService.updateBankAccountById(modifiedAccount.getId(), modifiedAccount),
                        repo.save(Movement.builder()
                                .amount(paymentAmount)
                                .targetBankAccountId(modifiedAccount.getId())
                                .timestamp(LocalDateTime.now())
                                .type(type)
                                .build())))
                .map(tuple -> MovementMapper.toMovementDto(tuple.getT2()));
    }

    private Mono<MovementDto> registerDebitCardMovement(MovementDebitCardDto movementDebitCardDto, String type) {
        return bankAccountService.getDebitCardById(movementDebitCardDto.getDebitCardId())
                .flatMap(debitCard -> bankAccountService.getBankAccountById(debitCard.getMainAccountId())
                        .flatMap(mainAccount -> mainAccount.getBalance() >= movementDebitCardDto.getAmount()
                                ? this.registerWithMainAccount(mainAccount, movementDebitCardDto.getAmount(), type)
                                : this.registerWithSecondaryAccount(debitCard.getSecondaryAccountIds(), movementDebitCardDto.getAmount(), type)));
    }

    @Override
    public Mono<MovementDto> updateById(String id, MovementDto movementDto) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Flux<MovementDto> getBankAccountFees(FeesBankAccountDto feesBankAccountDto) {
        return repo.findByTypeAndTargetBankAccountIdAndTimestampBetween(
                        Constants.TRANSACTION_FEE,
                        feesBankAccountDto.getBankAccountId(),
                        feesBankAccountDto.getStartDate().atStartOfDay(),
                        feesBankAccountDto.getEndDate().atStartOfDay())
                .map(MovementMapper::toMovementDto);
    }

    private Mono<MovementDto> validateDepositFee(Tuple2<BankAccountDto, List<Movement>> tuple2, Double depositAmount) {
        BankAccountDto bankAccountDto = tuple2.getT1();
        if (bankAccountDto.getTransactionLimit() > tuple2.getT2().size()) {
            return registerDepositWithoutFee(bankAccountDto, depositAmount);
        } else {
            return registerDepositWithFee(bankAccountDto, depositAmount);
        }
    }

    private Mono<MovementDto> registerDepositWithoutFee(BankAccountDto bankAccountDto, Double depositAmount) {
        return Mono.just(bankAccountDto)
                .map(accountDto -> accountDto.toBuilder().balance(accountDto.getBalance() + depositAmount).build())
                .flatMap(accountDto -> bankAccountService.updateBankAccountById(accountDto.getId(), accountDto))
                .map(accountDto -> Movement.builder().targetBankAccountId(accountDto.getId()).amount(depositAmount).timestamp(LocalDateTime.now()).type(Constants.DEPOSIT_TRANSACTION).build())
                .flatMap(repo::save)
                .map(MovementMapper::toMovementDto);
    }

    private Mono<MovementDto> registerDepositWithFee(BankAccountDto bankAccountDto, Double depositAmount) {
        return Mono.just(bankAccountDto)
                .filter(accountDto -> depositAmount > Constants.FEE)
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .map(accountDto -> accountDto.toBuilder().balance(accountDto.getBalance() + depositAmount - Constants.FEE).build())
                .flatMap(accountDto -> bankAccountService.updateBankAccountById(accountDto.getId(), accountDto))
                .map(accountDto -> Movement.builder().targetBankAccountId(accountDto.getId()).amount(depositAmount).timestamp(LocalDateTime.now()).type(Constants.DEPOSIT_TRANSACTION).build())
                .flatMap(movement -> repo.save(movement).flatMap(deposit -> registerFeeFromTransaction(deposit).map(fee -> MovementMapper.toMovementDto(deposit))));
    }

    private Mono<MovementDto> validateWithdrawalFee(Tuple2<BankAccountDto, List<Movement>> tuple2, Double withdrawalAmount) {
        BankAccountDto bankAccountDto = tuple2.getT1();
        if (bankAccountDto.getTransactionLimit() > tuple2.getT2().size()) {
            return registerWithdrawalWithoutFee(bankAccountDto, withdrawalAmount);
        } else {
            return registerWithdrawalWithFee(bankAccountDto, withdrawalAmount);
        }
    }

    private Mono<MovementDto> registerWithdrawalWithoutFee(BankAccountDto bankAccountDto, Double withdrawalAmount) {
        return Mono.just(bankAccountDto)
                .filter(accountDto -> accountDto.getBalance() - withdrawalAmount >= 0)
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .map(accountDto -> accountDto.toBuilder().balance(accountDto.getBalance() - withdrawalAmount).build())
                .flatMap(accountDto -> bankAccountService.updateBankAccountById(accountDto.getId(), accountDto))
                .map(accountDto -> Movement.builder().targetBankAccountId(accountDto.getId()).amount(withdrawalAmount).timestamp(LocalDateTime.now()).type(Constants.WITHDRAWAL_TRANSACTION).build())
                .flatMap(repo::save)
                .map(MovementMapper::toMovementDto);
    }

    private Mono<MovementDto> registerWithdrawalWithFee(BankAccountDto bankAccountDto, Double withdrawalAmount) {
        return Mono.just(bankAccountDto)
                .filter(accountDto -> accountDto.getBalance() >= withdrawalAmount + Constants.FEE)
                .switchIfEmpty(Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .map(accountDto -> accountDto.toBuilder().balance(accountDto.getBalance() - withdrawalAmount - Constants.FEE).build())
                .flatMap(accountDto -> bankAccountService.updateBankAccountById(accountDto.getId(), accountDto))
                .map(accountDto -> Movement.builder().targetBankAccountId(accountDto.getId()).amount(withdrawalAmount).timestamp(LocalDateTime.now()).type(Constants.WITHDRAWAL_TRANSACTION).build())
                .flatMap(movement -> repo.save(movement).flatMap(withdrawal -> registerFeeFromTransaction(withdrawal).map(fee -> MovementMapper.toMovementDto(withdrawal))));
    }

    private Mono<Movement> registerFeeFromTransaction(Movement transaction) {
        return repo.save(Movement.builder()
                .targetBankAccountId(transaction.getTargetBankAccountId())
                .amount(Constants.FEE)
                .timestamp(LocalDateTime.now())
                .type(Constants.TRANSACTION_FEE)
                .movementId(transaction.getId())
                .build());
    }

    private MergeBankAccountsDto validateOwnTransfer(BankAccountDto sourceBankAccount, BankAccountDto targetBankAccount, Double transferAmount) {
        if (!Objects.equals(sourceBankAccount.getCustomerId(), targetBankAccount.getCustomerId())) {
            throw new DomainException(HttpStatus.BAD_REQUEST, Constants.DIFFERENT_CUSTOMERS);
        }
        return validateTransfer(sourceBankAccount, targetBankAccount, transferAmount);
    }

    private MergeBankAccountsDto validateTransferToThirdParties(BankAccountDto sourceBankAccount, BankAccountDto targetBankAccount, Double transferAmount) {
        if (Objects.equals(sourceBankAccount.getCustomerId(), targetBankAccount.getCustomerId())) {
            throw new DomainException(HttpStatus.BAD_REQUEST, Constants.SAME_CUSTOMER);
        }
        return validateTransfer(sourceBankAccount, targetBankAccount, transferAmount);
    }

    private MergeBankAccountsDto validateTransfer(BankAccountDto sourceBankAccount, BankAccountDto targetBankAccount, Double transferAmount) {
        if (sourceBankAccount.getBalance() < transferAmount) {
            throw new DomainException(HttpStatus.BAD_REQUEST, String.format(Constants.INSUFFICIENT_BALANCE, sourceBankAccount.getBalance()));
        }
        sourceBankAccount.toBuilder().balance(sourceBankAccount.getBalance() - transferAmount).build();
        targetBankAccount.toBuilder().balance(targetBankAccount.getBalance() + transferAmount).build();
        return MergeBankAccountsDto.builder().bankAccount1(sourceBankAccount).bankAccount2(targetBankAccount).build();
    }

}
