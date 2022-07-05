package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.CreditCardDto;
import reactor.core.publisher.Mono;

public interface ICreditService {

    Mono<CreditCardDto> getCreditCardById(String id);

    Mono<CreditCardDto> updateCreditCardById(String id, CreditCardDto creditCardDto);

}
