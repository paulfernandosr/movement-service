package com.nttdata.movementservice.util;

import com.nttdata.movementservice.dto.*;
import com.nttdata.movementservice.model.BankAccountMovement;
import com.nttdata.movementservice.model.CreditCardMovement;

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
                .checkingAccountId(movementDto.getCheckingAccountId())
                .build();
    }

    public static BankAccountMovementDto toDto(BankAccountMovement movement) {
        return BankAccountMovementDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .checkingAccountId(movement.getCheckingAccountId())
                .build();
    }

    public static BankAccountMovement toModel(BankAccountDepositDto depositDto) {
        return BankAccountMovement.builder()
                .id(depositDto.getId())
                .amount(depositDto.getAmount())
                .timestamp(depositDto.getTimestamp())
                .savingAccountId(depositDto.getSavingAccountId())
                .fixedTermAccountId(depositDto.getFixedTermAccountId())
                .checkingAccountId(depositDto.getCheckingAccountId())
                .build();
    }

    public static BankAccountMovement toModel(BankAccountWithdrawalDto withdrawalDto) {
        return BankAccountMovement.builder()
                .id(withdrawalDto.getId())
                .amount(withdrawalDto.getAmount())
                .timestamp(withdrawalDto.getTimestamp())
                .savingAccountId(withdrawalDto.getSavingAccountId())
                .fixedTermAccountId(withdrawalDto.getFixedTermAccountId())
                .checkingAccountId(withdrawalDto.getCheckingAccountId())
                .build();
    }

    public static BankAccountDepositDto toDepositDto(BankAccountMovement movement) {
        return BankAccountDepositDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .checkingAccountId(movement.getCheckingAccountId())
                .build();
    }

    public static BankAccountWithdrawalDto toWithdrawalDto(BankAccountMovement movement) {
        return BankAccountWithdrawalDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .savingAccountId(movement.getSavingAccountId())
                .fixedTermAccountId(movement.getFixedTermAccountId())
                .checkingAccountId(movement.getCheckingAccountId())
                .build();
    }

    public static CreditCardMovement toModel(CreditCardMovementDto movementDto) {
        return CreditCardMovement.builder()
                .id(movementDto.getId())
                .amount(movementDto.getAmount())
                .timestamp(movementDto.getTimestamp())
                .creditCardId(movementDto.getCreditCardId())
                .build();
    }

    public static CreditCardMovementDto toDto(CreditCardMovement movement) {
        return CreditCardMovementDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .creditCardId(movement.getCreditCardId())
                .build();
    }

    public static CreditCardMovement toModel(CreditCardPaymentDto paymentDto) {
        return CreditCardMovement.builder()
                .id(paymentDto.getId())
                .amount(paymentDto.getAmount())
                .timestamp(paymentDto.getTimestamp())
                .creditCardId(paymentDto.getCreditCardId())
                .build();
    }

    public static CreditCardMovement toModel(CreditCardConsumptionDto consumptionDto) {
        return CreditCardMovement.builder()
                .id(consumptionDto.getId())
                .amount(consumptionDto.getAmount())
                .timestamp(consumptionDto.getTimestamp())
                .creditCardId(consumptionDto.getCreditCardId())
                .build();
    }

    public static CreditCardPaymentDto toPaymentDto(CreditCardMovement movement) {
        return CreditCardPaymentDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .creditCardId(movement.getCreditCardId())
                .build();
    }

    public static CreditCardConsumptionDto toConsumptionDto(CreditCardMovement movement) {
        return CreditCardConsumptionDto.builder()
                .id(movement.getId())
                .amount(movement.getAmount())
                .timestamp(movement.getTimestamp())
                .creditCardId(movement.getCreditCardId())
                .build();
    }

}
