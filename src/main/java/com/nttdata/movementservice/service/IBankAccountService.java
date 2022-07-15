package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.BankAccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

    Mono<BankAccountDto> getById(String id);

    Flux<BankAccountDto> getByCustomerId(String customerId);

    Mono<BankAccountDto> updateById(String id, BankAccountDto bankAccountDto);

}
