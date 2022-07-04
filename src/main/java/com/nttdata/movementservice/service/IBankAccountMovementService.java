package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountMovementService {

    Flux<BankAccountMovementDto> getAll();

    Mono<BankAccountMovementDto> getById(String id);

    Flux<BankAccountMovementDto> getBySavingAccountId(String personalSavingAccountId);

    Flux<BankAccountMovementDto> getByFixedTermAccountId(String personalFixedTermAccountId);

    Flux<BankAccountMovementDto> getByPersonalCheckingAccountId(String personalCheckingAccountId);

    Flux<BankAccountMovementDto> getByBusinessCheckingAccountId(String businessCheckingAccountId);

    Mono<BankAccountDepositDto> registerDeposit(BankAccountDepositDto bankAccountDepositDto);

    Mono<BankAccountWithdrawalDto> registerWithdrawal(BankAccountWithdrawalDto bankAccountWithdrawalDto);

    Mono<BankAccountMovementDto> updateById(String id, BankAccountMovementDto bankAccountMovementDto);

    Mono<Void> deleteById(String id);

}
