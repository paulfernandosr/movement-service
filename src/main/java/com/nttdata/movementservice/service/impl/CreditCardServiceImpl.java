package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.config.PropertiesConfig;
import com.nttdata.movementservice.dto.BankAccountDto;
import com.nttdata.movementservice.dto.CreditCardDto;
import com.nttdata.movementservice.dto.ErrorResponseBodyDto;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.service.ICreditService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.JacksonUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements ICreditService {

    private final WebClient webClient;
    private final PropertiesConfig propertiesConfig;

    public CreditCardServiceImpl(WebClient.Builder webClientBuilder, PropertiesConfig propertiesConfig) {
        this.webClient = webClientBuilder.baseUrl(propertiesConfig.creditServiceBaseUrl).build();
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public Mono<CreditCardDto> getCreditCardById(String id) {
        return webClient.get().uri(propertiesConfig.getCreditCardByIdMethod, id).retrieve()
                .bodyToMono(CreditCardDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

    @Override
    public Mono<CreditCardDto> updateCreditCardById(String id, CreditCardDto creditCardDto) {
        return webClient.put().uri(propertiesConfig.updateCreditCardByIdMethod, id)
                .body(Mono.just(creditCardDto), BankAccountDto.class)
                .retrieve()
                .bodyToMono(CreditCardDto.class)
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.error(new DomainException(e.getStatusCode(), String.format(Constants.ERROR_RESPONSE_IN_SERVICE,
                                e.getMessage(), JacksonUtil.jsonStringToObject(e.getResponseBodyAsString(), ErrorResponseBodyDto.class).getMessage()))));
    }

}
