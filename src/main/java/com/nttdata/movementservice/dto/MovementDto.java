package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class MovementDto {

    private String id;
    private Double amount;
    private LocalDateTime timestamp;
    private String type;
    private String movementId;
    private String sourceBankAccountId;
    private String targetBankAccountId;
    private String creditId;

}
