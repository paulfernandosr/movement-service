package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.config.PropertiesConfig;
import com.nttdata.movementservice.dto.CreditDto;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.service.ICreditService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Service
public class CreditServiceImpl implements ICreditService {

    private final WebClient webClient;
    private final PropertiesConfig propertiesConfig;

    public CreditServiceImpl(WebClient.Builder webClientBuilder, PropertiesConfig propertiesConfig) {
        this.webClient = webClientBuilder.baseUrl(propertiesConfig.creditServiceBaseUrl).build();
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    @CircuitBreaker(name = "creditService", fallbackMethod = "fallbackGetById")
    @TimeLimiter(name = "creditService", fallbackMethod = "fallbackGetById")
    public Mono<CreditDto> getById(String id) {
        return webClient.get().uri(propertiesConfig.getCreditByIdMethod, id)
                .retrieve()
                .bodyToMono(CreditDto.class);
    }

    @Override
    @CircuitBreaker(name = "creditService", fallbackMethod = "fallbackUpdateById")
    @TimeLimiter(name = "creditService", fallbackMethod = "fallbackUpdateById")
    public Mono<CreditDto> updateById(String id, CreditDto creditDto) {
        return webClient.put().uri(propertiesConfig.updateCreditByIdMethod, id)
                .body(Mono.just(creditDto), CreditDto.class)
                .retrieve()
                .bodyToMono(CreditDto.class);
    }

    private Mono<CreditDto> fallbackGetById(String id, WebClientResponseException e) {
        return Mono.error(new DomainException(e.getStatusCode(), e.getMessage()));
    }

    private Mono<CreditDto> fallbackGetById(String id, TimeoutException e) {
        return Mono.error(new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    private Flux<CreditDto> fallbackUpdateById(String id, CreditDto creditDto, WebClientResponseException e) {
        return Flux.error(new DomainException(e.getStatusCode(), e.getMessage()));
    }

    private Flux<CreditDto> fallbackUpdateById(String id, CreditDto creditDto, TimeoutException e) {
        return Flux.error(new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
