package com.nttdata.movementservice.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
public class FeesBankAccountDto {

    private final String bankAccountId;
    private final LocalDate startDate;
    private final LocalDate endDate;

}
