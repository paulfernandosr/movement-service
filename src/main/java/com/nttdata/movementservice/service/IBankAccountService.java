package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.BankAccountDto;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

    Mono<BankAccountDto> getSavingAccountById(String id);

    Mono<BankAccountDto> getFixedTermAccountById(String id);

    Mono<BankAccountDto> getCheckingAccountById(String id);

    Mono<BankAccountDto> updateSavingAccountById(String id, BankAccountDto bankAccountDto);

    Mono<BankAccountDto> updateFixedTermAccountById(String id, BankAccountDto bankAccountDto);

    Mono<BankAccountDto> updateCheckingAccountById(String id, BankAccountDto bankAccountDto);


}
