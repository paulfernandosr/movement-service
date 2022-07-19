package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.*;
import com.nttdata.movementservice.dto.request.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementService {

    Flux<MovementDto> getAll();

    Mono<MovementDto> getById(String id);

    Flux<MovementDto> getByCreditId(String creditId);

    Flux<MovementDto> getByBankAccountId(String bankAccountId);

    Flux<MovementDto> getBankAccountFees(FeesBankAccountDto feesBankAccountDto);

    Mono<MovementDto> registerDeposit(TransactionBankAccountDto transactionBankAccountDto);

    Mono<MovementDto> registerWithdrawal(TransactionBankAccountDto transactionBankAccountDto);

    Mono<MovementDto> registerTransferToOwnBankAccount(TransferBankAccountDto transferBankAccountDto);

    Mono<MovementDto> registerTransferToThirdPartiesBankAccount(TransferBankAccountDto transferBankAccountDto);

    Mono<MovementDto> registerCreditPayment(MovementCreditDto movementCreditDto);

    Mono<MovementDto> registerCreditCardPayment(MovementCreditDto movementCreditDto);

    Mono<MovementDto> registerCreditCardConsumption(MovementCreditDto movementCreditDto);

    Mono<MovementDto> registerDebitCardPayment(MovementDebitCardDto movementDebitCardDto);

    Mono<MovementDto> registerDebitCardWithdrawal(MovementDebitCardDto movementDebitCardDto);

    Mono<MovementDto> updateById(String id, MovementDto movementDto);

    Mono<Void> deleteById(String id);


}
