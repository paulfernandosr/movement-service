package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
public class CreditCardDto {

    private final String id;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expirationDate;
    private final Double consumed;
    private final Double creditLine;
    private final String personalCustomerId;
    private final String businessCustomerId;

}
