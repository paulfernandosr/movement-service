package com.nttdata.movementservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.RequestValidator;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountDepositDto {

    private String id;

    @NotNull(message = Constants.NOT_NULL)
    @Min(value = 0, message = Constants.DEPOSIT_AMOUNT_IS_LESS_THAN_ZERO)
    private Double amount;

    private LocalDateTime timestamp;
    private String savingAccountId;
    private String fixedTermAccountId;
    private String personalCheckingAccountId;
    private String businessCheckingAccountId;


    @AssertTrue(message = Constants.BANK_ACCOUNT_ID_IS_REQUIRED)
    private boolean isSavingAccountId() {
        return isBankAccountId();
    }

    @AssertTrue(message = Constants.BANK_ACCOUNT_ID_IS_REQUIRED)
    private boolean isFixedTermAccountId() {
        return isBankAccountId();
    }

    @AssertTrue(message = Constants.BANK_ACCOUNT_ID_IS_REQUIRED)
    private boolean isPersonalCheckingAccountId() {
        return isBankAccountId();
    }

    @AssertTrue(message = Constants.BANK_ACCOUNT_ID_IS_REQUIRED)
    private boolean isBusinessCheckingAccountId() {
        return isBankAccountId();
    }

    private boolean isBankAccountId() {
        return RequestValidator.isBankAccountId(savingAccountId, fixedTermAccountId, personalCheckingAccountId, businessCheckingAccountId);
    }

}
