package com.nttdata.movementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MovementCreditDto {

    private Double amount;
    private String creditId;

}
