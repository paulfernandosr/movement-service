package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountMovementService {

    Flux<BankAccountMovementDto> getAll();

    Mono<BankAccountMovementDto> getById(String id);

    Flux<BankAccountMovementDto> getBySavingAccountId(String savingAccountId);

    Flux<BankAccountMovementDto> getByFixedTermAccountId(String fixedTermAccountId);

    Flux<BankAccountMovementDto> getByCheckingAccountId(String checkingAccountId);

    Mono<BankAccountDepositDto> registerDeposit(BankAccountDepositDto bankAccountDepositDto);

    Mono<BankAccountWithdrawalDto> registerWithdrawal(BankAccountWithdrawalDto bankAccountWithdrawalDto);

    Mono<BankAccountMovementDto> updateById(String id, BankAccountMovementDto bankAccountMovementDto);

    Mono<Void> deleteById(String id);

}
