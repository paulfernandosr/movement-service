package com.nttdata.movementservice.util;

import com.nttdata.movementservice.dto.*;
import com.nttdata.movementservice.dto.request.*;
import com.nttdata.movementservice.model.Movement;

public class MovementMapper {

    private MovementMapper() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static Movement toMovement(MovementDto movementDto) {
        return Movement.builder()
                .id(movementDto.getId())
                .amount(movementDto.getAmount())
                .timestamp(movementDto.getTimestamp())
                .type(movementDto.getType())
                .movementId(movementDto.getMovementId())
                .sourceBankAccountId(movementDto.getSourceBankAccountId())
                .targetBankAccountId(movementDto.getTargetBankAccountId())
                .creditId(movementDto.getCreditId())
                .build();
    }

    public static MovementDto toMovementDto(Movement movement) {
        return MovementDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .type(movement.getType())
                .movementId(movement.getMovementId())
                .sourceBankAccountId(movement.getSourceBankAccountId())
                .targetBankAccountId(movement.getTargetBankAccountId())
                .creditId(movement.getCreditId())
                .build();
    }

    public static Movement toMovement(MovementCreditDto movementCreditDto) {
        return Movement.builder()
                .amount(movementCreditDto.getAmount())
                .creditId(movementCreditDto.getCreditId())
                .build();
    }

    public static Movement toMovement(TransferBankAccountDto transferBankAccountDto) {
        return Movement.builder()
                .amount(transferBankAccountDto.getAmount())
                .sourceBankAccountId(transferBankAccountDto.getSourceBankAccountId())
                .targetBankAccountId(transferBankAccountDto.getTargetBankAccountId())
                .build();
    }

}
