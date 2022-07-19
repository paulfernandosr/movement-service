package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.BankAccountDto;
import com.nttdata.movementservice.dto.DebitCardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

    Mono<BankAccountDto> getBankAccountById(String id);

    Mono<DebitCardDto> getDebitCardById(String id);

    Flux<BankAccountDto> getBankAccountByCustomerId(String customerId);

    Mono<BankAccountDto> updateBankAccountById(String id, BankAccountDto bankAccountDto);

}
