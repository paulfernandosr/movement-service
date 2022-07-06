package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import com.nttdata.movementservice.exception.BadRequestException;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.exception.MovementNotFoundException;
import com.nttdata.movementservice.repo.IBankAccountMovementRepo;
import com.nttdata.movementservice.service.IBankAccountMovementService;
import com.nttdata.movementservice.service.IBankAccountService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountMovementServiceImpl implements IBankAccountMovementService {

    private final IBankAccountMovementRepo repo;
    private final IBankAccountService bankAccountService;

    @Override
    public Flux<BankAccountMovementDto> getAll() {
        return repo.findAll().map(MovementMapper::toDto);
    }

    @Override
    public Mono<BankAccountMovementDto> getById(String id) {
        return repo.findById(id)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, id))));
    }

    @Override
    public Flux<BankAccountMovementDto> getBySavingAccountId(String savingAccountId) {
        return repo.findBySavingAccountId(savingAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, savingAccountId))));
    }

    @Override
    public Flux<BankAccountMovementDto> getByFixedTermAccountId(String fixedTermAccountId) {
        return repo.findByFixedTermAccountId(fixedTermAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, fixedTermAccountId))));
    }

    @Override
    public Flux<BankAccountMovementDto> getByCheckingAccountId(String checkingAccountId) {
        return repo.findByCheckingAccountId(checkingAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, checkingAccountId))));
    }

    @Override
    public Mono<BankAccountDepositDto> registerDeposit(BankAccountDepositDto bankAccountDepositDto) {
        if (bankAccountDepositDto.getSavingAccountId() != null) {
            return registerDepositForSavingAccount(bankAccountDepositDto);
        } else if (bankAccountDepositDto.getFixedTermAccountId() != null) {
            return registerDepositForFixedTermAccount(bankAccountDepositDto);
        } else if (bankAccountDepositDto.getCheckingAccountId() != null) {
            return registerDepositForCheckingAccountId(bankAccountDepositDto);
        }
        return Mono.error(new BadRequestException(Constants.BANK_ACCOUNT_ID_IS_REQUIRED));
    }

    @Override
    public Mono<BankAccountWithdrawalDto> registerWithdrawal(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        if (bankAccountWithdrawalDto.getSavingAccountId() != null) {
            return registerWithdrawalForSavingAccount(bankAccountWithdrawalDto);
        } else if (bankAccountWithdrawalDto.getFixedTermAccountId() != null) {
            return registerWithdrawalForFixedTermAccount(bankAccountWithdrawalDto);
        } else if (bankAccountWithdrawalDto.getCheckingAccountId() != null) {
            return registerWithdrawalForCheckingAccountId(bankAccountWithdrawalDto);
        }
        return Mono.error(new BadRequestException(Constants.BANK_ACCOUNT_ID_IS_REQUIRED));
    }

    private Mono<BankAccountDepositDto> registerDepositForSavingAccount(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getSavingAccountById(bankAccountDepositDto.getSavingAccountId())
                .flatMap(savingAccountDto -> bankAccountService.updateSavingAccountById(savingAccountDto.getId(),
                        savingAccountDto.toBuilder()
                                .balance(savingAccountDto.getBalance() + bankAccountDepositDto.getAmount())
                                .build()))
                .flatMap(updatedSavingAccountDto -> repo.save(MovementMapper.toModel(bankAccountDepositDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toDepositDto);
    }

    private Mono<BankAccountWithdrawalDto> registerWithdrawalForSavingAccount(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        return bankAccountService.getSavingAccountById(bankAccountWithdrawalDto.getSavingAccountId())
                .flatMap(savingAccountDto -> savingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateSavingAccountById(savingAccountDto.getId(),
                        savingAccountDto.toBuilder()
                                .balance(savingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedSavingAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    private Mono<BankAccountDepositDto> registerDepositForFixedTermAccount(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getFixedTermAccountById(bankAccountDepositDto.getFixedTermAccountId())
                .flatMap(fixedTermAccountDto -> bankAccountService.updateFixedTermAccountById(fixedTermAccountDto.getId(),
                        fixedTermAccountDto.toBuilder()
                                .balance(fixedTermAccountDto.getBalance() + bankAccountDepositDto.getAmount())
                                .build()))
                .flatMap(updatedFixedTermAccountDto -> repo.save(MovementMapper.toModel(bankAccountDepositDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toDepositDto);
    }

    private Mono<BankAccountWithdrawalDto> registerWithdrawalForFixedTermAccount(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        return bankAccountService.getFixedTermAccountById(bankAccountWithdrawalDto.getFixedTermAccountId())
                .flatMap(fixedTermAccountDto -> fixedTermAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateFixedTermAccountById(fixedTermAccountDto.getId(),
                        fixedTermAccountDto.toBuilder()
                                .balance(fixedTermAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedFixedTermAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    private Mono<BankAccountDepositDto> registerDepositForCheckingAccountId(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getCheckingAccountById(bankAccountDepositDto.getCheckingAccountId())
                .flatMap(checkingAccountDto -> bankAccountService.updateCheckingAccountById(checkingAccountDto.getId(),
                        checkingAccountDto.toBuilder()
                                .balance(checkingAccountDto.getBalance() + bankAccountDepositDto.getAmount())
                                .build()))
                .flatMap(updatedCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountDepositDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toDepositDto);
    }

    private Mono<BankAccountWithdrawalDto> registerWithdrawalForCheckingAccountId(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        return bankAccountService.getCheckingAccountById(bankAccountWithdrawalDto.getCheckingAccountId())
                .flatMap(checkingAccountDto -> checkingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateSavingAccountById(checkingAccountDto.getId(),
                        checkingAccountDto.toBuilder()
                                .balance(checkingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    @Override
    public Mono<BankAccountMovementDto> updateById(String id, BankAccountMovementDto bankAccountMovementDto) {
        Mono<BankAccountMovementDto> movementDtoReqMono = Mono.just(bankAccountMovementDto);
        Mono<BankAccountMovementDto> movementDtoDbMono = getById(id);
        return movementDtoReqMono.zipWith(movementDtoDbMono, (movementDtoReq, movementDtoDb) ->
                        MovementMapper.toModel(movementDtoDb.toBuilder()
                                .amount(movementDtoReq.getAmount())
                                .timestamp(movementDtoReq.getTimestamp())
                                .savingAccountId(movementDtoReq.getSavingAccountId())
                                .fixedTermAccountId(movementDtoReq.getFixedTermAccountId())
                                .checkingAccountId(movementDtoReq.getCheckingAccountId())
                                .build()))
                .flatMap(movement -> repo.save(movement).map(MovementMapper::toDto));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return getById(id).flatMap(movementDto -> repo.deleteById(id));
    }

}
