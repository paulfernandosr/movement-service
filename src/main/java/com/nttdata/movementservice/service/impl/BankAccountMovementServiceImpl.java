package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import com.nttdata.movementservice.exception.BadRequestException;
import com.nttdata.movementservice.exception.MovementNotFoundException;
import com.nttdata.movementservice.repo.IBankAccountMovementRepo;
import com.nttdata.movementservice.service.IBankAccountMovementService;
import com.nttdata.movementservice.service.IBankAccountService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.MovementMapper;
import lombok.RequiredArgsConstructor;
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
        return repo.findBySavingAccountId(fixedTermAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, fixedTermAccountId))));
    }

    @Override
    public Flux<BankAccountMovementDto> getByPersonalCheckingAccountId(String personalCheckingAccountId) {
        return repo.findBySavingAccountId(personalCheckingAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, personalCheckingAccountId))));
    }

    @Override
    public Flux<BankAccountMovementDto> getByBusinessCheckingAccountId(String businessCheckingAccountId) {
        return repo.findBySavingAccountId(businessCheckingAccountId)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, businessCheckingAccountId))));
    }

    @Override
    public Mono<BankAccountDepositDto> registerDeposit(BankAccountDepositDto bankAccountDepositDto) {
        if (bankAccountDepositDto.getSavingAccountId() != null) {
            return registerDepositForSavingAccount(bankAccountDepositDto);
        } else if (bankAccountDepositDto.getFixedTermAccountId() != null) {
            return registerDepositForFixedTermAccount(bankAccountDepositDto);
        } else if (bankAccountDepositDto.getPersonalCheckingAccountId() != null) {
            return registerDepositForPersonalCheckingAccountId(bankAccountDepositDto);
        } else if (bankAccountDepositDto.getBusinessCheckingAccountId() != null) {
            return registerDepositForBusinessCheckingAccount(bankAccountDepositDto);
        }
        return Mono.error(new BadRequestException(Constants.BANK_ACCOUNT_ID_IS_REQUIRED));
    }

    @Override
    public Mono<BankAccountWithdrawalDto> registerWithdrawal(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        if (bankAccountWithdrawalDto.getSavingAccountId() != null) {
            return registerWithdrawalForSavingAccount(bankAccountWithdrawalDto);
        } else if (bankAccountWithdrawalDto.getFixedTermAccountId() != null) {
            return registerWithdrawalForFixedTermAccount(bankAccountWithdrawalDto);
        } else if (bankAccountWithdrawalDto.getPersonalCheckingAccountId() != null) {
            return registerWithdrawalForPersonalCheckingAccountId(bankAccountWithdrawalDto);
        } else if (bankAccountWithdrawalDto.getBusinessCheckingAccountId() != null) {
            return registerWithdrawalForBusinessCheckingAccount(bankAccountWithdrawalDto);
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
                        : Mono.error(new BadRequestException(Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedSavingAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    private Mono<BankAccountDepositDto> registerDepositForFixedTermAccount(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getFixedTermAccountById(bankAccountDepositDto.getSavingAccountId())
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
        return bankAccountService.getFixedTermAccountById(bankAccountWithdrawalDto.getSavingAccountId())
                .flatMap(fixedTermAccountDto -> fixedTermAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateSavingAccountById(fixedTermAccountDto.getId(),
                        fixedTermAccountDto.toBuilder()
                                .balance(fixedTermAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new BadRequestException(Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedFixedTermAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    private Mono<BankAccountDepositDto> registerDepositForPersonalCheckingAccountId(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getPersonalCheckingAccountById(bankAccountDepositDto.getSavingAccountId())
                .flatMap(personalCheckingAccountDto -> bankAccountService.updateFixedTermAccountById(personalCheckingAccountDto.getId(),
                        personalCheckingAccountDto.toBuilder()
                                .balance(personalCheckingAccountDto.getBalance() + bankAccountDepositDto.getAmount())
                                .build()))
                .flatMap(updatedPersonalCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountDepositDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toDepositDto);
    }

    private Mono<BankAccountWithdrawalDto> registerWithdrawalForPersonalCheckingAccountId(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        return bankAccountService.getPersonalCheckingAccountById(bankAccountWithdrawalDto.getSavingAccountId())
                .flatMap(personalCheckingAccountDto -> personalCheckingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateSavingAccountById(personalCheckingAccountDto.getId(),
                        personalCheckingAccountDto.toBuilder()
                                .balance(personalCheckingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new BadRequestException(Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedPersonalCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }

    private Mono<BankAccountDepositDto> registerDepositForBusinessCheckingAccount(BankAccountDepositDto bankAccountDepositDto) {
        return bankAccountService.getBusinessCheckingAccountById(bankAccountDepositDto.getSavingAccountId())
                .flatMap(businessCheckingAccountDto -> bankAccountService.updateFixedTermAccountById(businessCheckingAccountDto.getId(),
                        businessCheckingAccountDto.toBuilder()
                                .balance(businessCheckingAccountDto.getBalance() + bankAccountDepositDto.getAmount())
                                .build()))
                .flatMap(updatedBusinessCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountDepositDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toDepositDto);
    }

    private Mono<BankAccountWithdrawalDto> registerWithdrawalForBusinessCheckingAccount(BankAccountWithdrawalDto bankAccountWithdrawalDto) {
        return bankAccountService.getBusinessCheckingAccountById(bankAccountWithdrawalDto.getSavingAccountId())
                .flatMap(businessCheckingAccountDto -> businessCheckingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount() >= 0
                        ? bankAccountService.updateSavingAccountById(businessCheckingAccountDto.getId(),
                        businessCheckingAccountDto.toBuilder()
                                .balance(businessCheckingAccountDto.getBalance() + bankAccountWithdrawalDto.getAmount())
                                .build())
                        : Mono.error(new BadRequestException(Constants.WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedBusinessCheckingAccountDto -> repo.save(MovementMapper.toModel(bankAccountWithdrawalDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toWithdrawalDto);
    }


    @Override
    public Mono<BankAccountMovementDto> updateById(String id, BankAccountMovementDto bankAccountMovementDto) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

}
