package com.nttdata.movementservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MergeBankAccountsDto {

    private BankAccountDto bankAccount1;
    private BankAccountDto bankAccount2;

}
