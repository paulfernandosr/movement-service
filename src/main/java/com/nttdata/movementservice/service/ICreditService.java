package com.nttdata.movementservice.service;

import com.nttdata.movementservice.dto.CreditDto;
import reactor.core.publisher.Mono;

public interface ICreditService {

    Mono<CreditDto> getById(String id);

    Mono<CreditDto> updateById(String id, CreditDto creditDto);

}
