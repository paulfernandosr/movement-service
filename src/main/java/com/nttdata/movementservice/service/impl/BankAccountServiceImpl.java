package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.config.PropertiesConfig;
import com.nttdata.movementservice.dto.BankAccountDto;
import com.nttdata.movementservice.dto.ErrorResponseBodyDto;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.service.IBankAccountService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.JacksonUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements IBankAccountService {

    private final WebClient webClient;
    private final PropertiesConfig propertiesConfig;

    public BankAccountServiceImpl(WebClient.Builder webClientBuilder, PropertiesConfig propertiesConfig) {
        this.webClient = webClientBuilder.baseUrl(propertiesConfig.bankAccountsServiceBaseUrl).build();
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public Mono<BankAccountDto> getSavingAccountById(String id) {
        return webClient.get().uri(propertiesConfig.getSavingAccountByIdMethod, id).retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> getFixedTermAccountById(String id) {
        return webClient.get().uri(propertiesConfig.getFixedTermAccountByIdMethod, id).retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> getPersonalCheckingAccountById(String id) {
        return webClient.get().uri(propertiesConfig.getPersonalCheckingAccountByIdMethod, id).retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> getBusinessCheckingAccountById(String id) {
        return webClient.get().uri(propertiesConfig.getBusinessCheckingAccountByIdMethod, id).retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> updateSavingAccountById(String id, BankAccountDto bankAccountDto) {
        return webClient.put().uri(propertiesConfig.updateSavingAccountByIdMethod, id)
                .body(Mono.just(bankAccountDto), BankAccountDto.class)
                .retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> updateFixedTermAccountById(String id, BankAccountDto bankAccountDto) {
        return webClient.put().uri(propertiesConfig.updateFixedTermAccountByIdMethod, id)
                .body(Mono.just(bankAccountDto), BankAccountDto.class)
                .retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<BankAccountDto> updateCheckingAccountById(String id, BankAccountDto bankAccountDto) {
        return webClient.put().uri(propertiesConfig.updateCheckingAccountByIdMethod, id)
                .body(Mono.just(bankAccountDto), BankAccountDto.class)
                .retrieve()
                .bodyToMono(BankAccountDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

}
