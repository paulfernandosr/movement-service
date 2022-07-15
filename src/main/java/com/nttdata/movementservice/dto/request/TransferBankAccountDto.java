package com.nttdata.movementservice.dto.request;

import com.nttdata.movementservice.util.Constants;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class TransferBankAccountDto {

    @NotNull(message = Constants.NOT_NULL)
    @Min(value = 0, message = "Valor minimo es 0")
    private Double amount;

    @NotNull(message = Constants.NOT_NULL)
    private String sourceBankAccountId;

    @NotNull(message = Constants.NOT_NULL)
    private String targetBankAccountId;

    @AssertTrue(message = "La cuenta de origen y destino deben ser diferentes")
    private boolean isSourceBankAccountId() {
        return isValidBankAccount();
    }

    @AssertTrue(message = "La cuenta de origen y destino deben ser diferentes")
    private boolean isTargetBankAccountId() {
        return isValidBankAccount();
    }

    private boolean isValidBankAccount() {
        return !Objects.equals(sourceBankAccountId, targetBankAccountId);
    }

}
