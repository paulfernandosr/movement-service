package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class BankAccountDto {

    private final String id;
    private final String accountNumber;
    private final String cci;
    private final Double balance;
    private final String type;
    private final String customerId;
    private final String maintenanceFee;
    private final Integer monthlyMovementLimit;
    private final Double monthlyMinimumBalance;
    private final Integer transactionLimit;

}
