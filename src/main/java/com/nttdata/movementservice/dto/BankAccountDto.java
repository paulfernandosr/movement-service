package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class BankAccountDto {

    private String id;
    private String accountNumber;
    private String cci;
    private Double balance;
    private String personalCustomerId;
    private String businessCustomerId;

}
