package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class DebitCardDto {

    private final String id;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate expirationDate;
    private final String mainAccountId;
    private final List<String> secondaryAccountIds;

}
