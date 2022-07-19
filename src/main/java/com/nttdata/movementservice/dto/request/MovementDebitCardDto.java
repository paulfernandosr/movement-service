package com.nttdata.movementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MovementDebitCardDto {

    private Double amount;
    private String debitCardId;

}
