package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.config.PropertiesConfig;
import com.nttdata.movementservice.dto.BankAccountDto;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.service.IBankAccountService;
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
public class BankAccountServiceImpl implements IBankAccountService {

    private final WebClient webClient;
    private final PropertiesConfig propertiesConfig;

    public BankAccountServiceImpl(WebClient.Builder webClientBuilder, PropertiesConfig propertiesConfig) {
        this.webClient = webClientBuilder.baseUrl(propertiesConfig.bankAccountServiceBaseUrl).build();
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    @CircuitBreaker(name = "bankAccountService", fallbackMethod = "fallbackGetById")
    @TimeLimiter(name = "bankAccountService", fallbackMethod = "fallbackGetById")
    public Mono<BankAccountDto> getById(String id) {
        return webClient.get().uri(propertiesConfig.getBankAccountByIdMethod, id)
                .retrieve()
                .bodyToMono(BankAccountDto.class);
    }

    @Override
    @CircuitBreaker(name = "bankAccountService", fallbackMethod = "fallbackGetByCustomerId")
    @TimeLimiter(name = "bankAccountService", fallbackMethod = "fallbackGetByCustomerId")
    public Flux<BankAccountDto> getByCustomerId(String customerId) {
        return webClient.get().uri(propertiesConfig.getBankAccountsByCustomerIdMethod, customerId)
                .retrieve()
                .bodyToFlux(BankAccountDto.class);
    }

    @Override
    @CircuitBreaker(name = "bankAccountService", fallbackMethod = "fallbackUpdateById")
    @TimeLimiter(name = "bankAccountService", fallbackMethod = "fallbackUpdateById")
    public Mono<BankAccountDto> updateById(String id, BankAccountDto bankAccountDto) {
        return webClient.put().uri(propertiesConfig.updateBankAccountByIdMethod, id)
                .body(Mono.just(bankAccountDto), BankAccountDto.class)
                .retrieve()
                .bodyToMono(BankAccountDto.class);
    }

    private Mono<BankAccountDto> fallbackGetById(String id, WebClientResponseException e) {
        return Mono.error(new DomainException(e.getStatusCode(), e.getMessage()));
    }

    private Mono<BankAccountDto> fallbackGetById(String id, TimeoutException e) {
        return Mono.error(new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    private Flux<BankAccountDto> fallbackGetByCustomerId(String customerId, WebClientResponseException e) {
        return Flux.error(new DomainException(e.getStatusCode(), e.getMessage()));
    }

    private Flux<BankAccountDto> fallbackGetByCustomerId(String customerId, TimeoutException e) {
        return Flux.error(new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    private Mono<BankAccountDto> fallbackUpdateById(String id, BankAccountDto bankAccountDto, WebClientResponseException e) {
        return Mono.error(new DomainException(e.getStatusCode(), e.getMessage()));
    }

    private Mono<BankAccountDto> fallbackUpdateById(String id, BankAccountDto bankAccountDto, TimeoutException e) {
        return Mono.error(new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
