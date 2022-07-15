package com.nttdata.movementservice.util;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String FIVE_CURLY_BRACKETS = "{}{}{}{}{}";
    public static final String SLASH = "/";
    public static final String COLON = ": ";
    public static final String IN = " in ";

    // Keys
    public static final String TIMESTAMP = "timestamp";
    public static final String REQUEST_ID = "requestId";
    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String VALIDATIONS = "validations";
    public static final String ID = "id";
    public static final String CREDIT_ID = "creditId";
    public static final String BANK_ACCOUNT_ID = "bankAccountId";

    // Exception messages
    public static final String INVALID_CREDIT_TYPE = "Invalid credit type";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance with value: %s";
    public static final String SAME_CUSTOMER = "The source and target account belong to the same customer";
    public static final String DIFFERENT_CUSTOMERS = "The source and target account belong to different customers";

    public static final String MOVEMENT_NOT_FOUND = "Movement not found with %s: %s";
    public static final String WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE = "Withdrawal amount is greater than balance";

    public static final String BALANCE_IS_GREATER_THAN_CREDIT_LINE = "Balance is greater than the credit line";
    public static final String CONSUMPTION_AMOUNT_IS_GREATER_THAN_BALANCE = "Consumption amount is greater than the balance";

    public static final String DEPOSIT_AMOUNT_IS_LESS_THAN_ZERO = "Deposit amount is less than 0";
    public static final String WITHDRAWAL_AMOUNT_IS_GREATER_THAN_ZERO = "Withdrawal amount is greater than 0";
    public static final String PAYMENT_AMOUNT_IS_LESS_THAN_ZERO = "Payment amount is less than 0";
    public static final String CONSUMPTION_AMOUNT_IS_GREATER_THAN_ZERO = "Consumption amount is greater than 0";
    public static final String NULL_REQUEST = "The request is null";
    public static final String VIOLATED_CONSTRAINTS = "Restrictions were violated";
    public static final String NOT_NULL = "Must not be null";
    public static final String UTILITY_CLASS = "Utility class";

    // Collections
    public static final String MOVEMENTS_COLLECTION = "movements";
    public static final String BANK_ACCOUNT_MOVEMENTS_COLLECTION = "bankAccountMovements";
    public static final String CREDIT_CARD_MOVEMENTS_COLLECTION = "creditCardMovements";

    // Controller paths
    public static final String MOVEMENT_CONTROLLER = "/movements";

    // Method paths
    public static final String GET_ALL_METHOD = "/all";
    public static final String GET_BY_ID_METHOD = "/{" + ID + "}";
    public static final String GET_BY_CREDIT_ID_METHOD = "/credits/{creditId}";
    public static final String GET_BY_BANK_ACCOUNT_ID_METHOD = "/bankAccounts/{bankAccountId}";
    public static final String GET_BANK_ACCOUNT_FEES_METHOD = "/bank-account-fees";
    public static final String REGISTER_DEPOSIT_METHOD = "/deposits";
    public static final String REGISTER_WITHDRAWAL_METHOD = "/withdrawals";
    public static final String REGISTER_TRANSFER_TO_OWN_BANK_ACCOUNT_METHOD = "/transfers-to-own-bank-accounts";
    public static final String REGISTER_TRANSFER_TO_THIRD_PARTIES_BANK_ACCOUNT_METHOD = "/transfers-to-third-parties-bank-accounts";
    public static final String REGISTER_CREDIT_PAYMENT = "/credit-payments";
    public static final String REGISTER_CREDIT_CARD_PAYMENT = "/credit-card-payments";
    public static final String REGISTER_CREDIT_CARD_CONSUMPTION = "/credit-card-consumptions";
    public static final String UPDATE_BY_ID_METHOD = "/{" + ID + "}";
    public static final String DELETE_BY_ID_METHOD = "/{" + ID + "}";

    // Bank account service
    public static final String BANK_ACCOUNT_SERVICE_BASE_URL = "${bankAccountService.baseUrl}";
    public static final String GET_BANK_ACCOUNT_BY_ID_METHOD = "${bankAccountService.method.getBankAccountById}";
    public static final String GET_BANK_ACCOUNTS_BY_CUSTOMER_ID_METHOD = "${bankAccountService.method.getBankAccountsByCustomerId}";
    public static final String UPDATE_BANK_ACCOUNT_BY_ID_METHOD = "${bankAccountService.method.updateBankAccountById}";

    // Credit service
    public static final String CREDIT_SERVICE_BASE_URL = "${creditService.baseUrl}";
    public static final String GET_CREDIT_BY_ID_METHOD = "${creditService.method.getCreditById}";
    public static final String UPDATE_CREDIT_BY_ID_METHOD = "${creditService.method.updateCreditById}";

    // Types
    public static final String DEPOSIT_TRANSACTION = "TRANSACTION.DEPOSIT";
    public static final String WITHDRAWAL_TRANSACTION = "TRANSACTION.WITHDRAWAL";
    public static final String TRANSFER_TO_OWN_BANK_ACCOUNTS = "TRANSFER.BANK_ACCOUNTS.OWN";
    public static final String TRANSFER_TO_THIRD_PARTIES_BANK_ACCOUNTS = "TRANSFER.BANK_ACCOUNTS.THIRD_PARTIES";
    public static final String CREDIT_PAYMENT = "PAYMENT.CREDIT";
    public static final String CREDIT_CARD_PAYMENT = "PAYMENT.CREDIT_CARD";
    public static final String CREDIT_CARD_CONSUMPTION = "CONSUMPTION.CREDIT_CARD";
    public static final String PERSONAL_CREDIT = "CREDIT.PERSONAL";
    public static final String BUSINESS_CREDIT = "CREDIT.BUSINESS";
    public static final String PERSONAL_CREDIT_CARD = "CREDIT_CARD.PERSONAL";
    public static final String BUSINESS_CREDIT_CARD = "CREDIT_CARD.BUSINESS";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String TRANSACTION_FEE = "TRANSACTION_FEE";
    public static final double FEE = 3.50;

}
