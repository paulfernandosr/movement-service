package com.nttdata.movementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TransactionBankAccountDto {

    private Double amount;
    private String bankAccountId;

}
