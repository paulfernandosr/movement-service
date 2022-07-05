package com.nttdata.movementservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nttdata.movementservice.util.Constants;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardMovementDto {

    private String id;

    @NotNull(message = Constants.NOT_NULL)
    private Double amount;

    private LocalDateTime timestamp;

    @NotNull(message = Constants.NOT_NULL)
    private String creditCardId;

}
