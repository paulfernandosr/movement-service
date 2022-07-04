package com.nttdata.movementservice.util;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import com.nttdata.movementservice.model.BankAccountMovement;

public class MovementMapper {

    private MovementMapper() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static BankAccountMovement toModel(BankAccountMovementDto movementDto) {
        return BankAccountMovement.builder()
                .id(movementDto.getId())
                .amount(movementDto.getAmount())
                .timestamp(movementDto.getTimestamp())
                .savingAccountId(movementDto.getSavingAccountId())
                .fixedTermAccountId(movementDto.getFixedTermAccountId())
                .personalCheckingAccountId(movementDto.getPersonalCheckingAccountId())
                .businessCheckingAccountId(movementDto.getBusinessCheckingAccountId())
                .build();
    }

    public static BankAccountMovementDto toDto(BankAccountMovement movement) {
        return BankAccountMovementDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .personalCheckingAccountId(movement.getPersonalCheckingAccountId())
                .businessCheckingAccountId(movement.getBusinessCheckingAccountId())
                .build();
    }

    public static BankAccountMovement toModel(BankAccountDepositDto depositDto) {
        return BankAccountMovement.builder()
                .id(depositDto.getId())
                .amount(depositDto.getAmount())
                .timestamp(depositDto.getTimestamp())
                .savingAccountId(depositDto.getSavingAccountId())
                .fixedTermAccountId(depositDto.getFixedTermAccountId())
                .personalCheckingAccountId(depositDto.getPersonalCheckingAccountId())
                .businessCheckingAccountId(depositDto.getBusinessCheckingAccountId())
                .build();
    }

    public static BankAccountMovement toModel(BankAccountWithdrawalDto withdrawalDto) {
        return BankAccountMovement.builder()
                .id(withdrawalDto.getId())
                .amount(withdrawalDto.getAmount())
                .timestamp(withdrawalDto.getTimestamp())
                .savingAccountId(withdrawalDto.getSavingAccountId())
                .fixedTermAccountId(withdrawalDto.getFixedTermAccountId())
                .personalCheckingAccountId(withdrawalDto.getPersonalCheckingAccountId())
                .businessCheckingAccountId(withdrawalDto.getBusinessCheckingAccountId())
                .build();
    }

    public static BankAccountDepositDto toDepositDto(BankAccountMovement movement) {
        return BankAccountDepositDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .personalCheckingAccountId(movement.getPersonalCheckingAccountId())
                .businessCheckingAccountId(movement.getBusinessCheckingAccountId())
                .build();
    }

    public static BankAccountWithdrawalDto toWithdrawalDto(BankAccountMovement movement) {
        return BankAccountWithdrawalDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .personalCheckingAccountId(movement.getPersonalCheckingAccountId())
                .businessCheckingAccountId(movement.getBusinessCheckingAccountId())
                .build();
    }

}
