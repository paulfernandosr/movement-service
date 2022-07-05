package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardMovementService {

    Flux<CreditCardMovementDto> getAll();

    Mono<CreditCardMovementDto> getById(String id);

    Flux<CreditCardMovementDto> getByCreditCardId(String creditCardId);

    Mono<CreditCardPaymentDto> registerPayment(CreditCardPaymentDto creditCardPaymentDto);

    Mono<CreditCardConsumptionDto> registerConsumption(CreditCardConsumptionDto creditCardConsumptionDto);

    Mono<CreditCardMovementDto> updateById(String id, CreditCardMovementDto creditCardMovementDto);

    Mono<Void> deleteById(String id);

}
