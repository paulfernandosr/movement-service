package com.nttdata.movementservice.util;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String OPENING_CURLY_BRACKET = "{";
    public static final String CLOSING_CURLY_BRACKET = "}";
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
    public static final String CARD_NUMBER = "cardNumber";
    public static final String PERSONAL_CUSTOMER_ID = "personalCustomerId";

    // Exception messages
    public static final String MOVEMENT_NOT_FOUND = "Movement not found with %s: %s";

    public static final String WITHDRAWAL_AMOUNT_IS_GREATER_THAN_BALANCE = "Withdrawal amount is greater than balance";
    public static final String DEPOSIT_AMOUNT_IS_LESS_THAN_ZERO = "Deposit amount is less than 0";
    public static final String WITHDRAWAL_AMOUNT_IS_GREATER_THAN_ZERO = "Withdrawal amount is greater than 0";
    public static final String NULL_REQUEST = "The request is null";
    public static final String VIOLATED_CONSTRAINTS = "Restrictions were violated";
    public static final String NOT_NULL = "Must not be null";
    public static final String BANK_ACCOUNT_ID_IS_REQUIRED = "savingAccountId or fixedTermAccountId or personalCheckingAccountId or businessCheckingAccountId is required";
    public static final String CREDIT_DUPLICATED_BY_A_FIELD = "There is already a bank account with %s: %s";
    public static final String CREDIT_DUPLICATED_BY_TWO_FIELDS = "There is already a bank account with %s: %s or %s: %s";
    public static final String UTILITY_CLASS = "Utility class";

    public static final String ERROR_RESPONSE_IN_SERVICE = "%s -> %s";

    // Collections
    public static final String BANK_ACCOUNT_MOVEMENTS_COLLECTION = "bankAccountMovements";
    public static final String CREDIT_CARD_MOVEMENTS_COLLECTION = "creditCardMovements";
    public static final String MOVEMENT_TYPES_COLLECTION = "movementTypes";

    // Controller paths
    public static final String BANK_ACCOUNT_MOVEMENT_CONTROLLER = "${controller.bank-account-movement.base-path}";

    // Method paths
    public static final String GET_ALL_METHOD = "${controller.method.get-all}";
    public static final String GET_BY_ID_METHOD = "${controller.method.get-by-id}";
    public static final String REGISTER_METHOD = "${controller.method.register}";
    public static final String UPDATE_BY_ID_METHOD = "${controller.method.update-by-id}";
    public static final String DELETE_BY_ID_METHOD = "${controller.method.delete-by-id}";

    public static final String GET_BY_SAVING_ACCOUNT_ID_METHOD = "${controller.bank-account-movement.method.get-by-saving-account-id}";
    public static final String GET_BY_FIXED_TERM_ACCOUNT_ID_METHOD = "${controller.bank-account-movement.method.get-by-fixed-term-account-id}";
    public static final String GET_BY_PERSONAL_CHECKING_ACCOUNT_ID_METHOD = "${controller.bank-account-movement.method.get-by-personal-checking-account-id}";
    public static final String GET_BY_BUSINESS_CHECKING_ACCOUNT_ID_METHOD = "${controller.bank-account-movement.method.get-by-business-checking-account-id}";
    public static final String REGISTER_DEPOSIT_METHOD = "${controller.bank-account-movement.method.register-deposit}";
    public static final String REGISTER_WITHDRAWAL_METHOD = "${controller.bank-account-movement.method.register-withdrawal}";

    // Path variables
    public static final String PATH_ID_VARIABLE = "${controller.path-variable.id}";

    // Bank accounts service
    public static final String BANK_ACCOUNT_SERVICE_BASE_URL = "${bank-account-service.base-url}";
    public static final String GET_SAVING_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.get-saving-account-by-id}";
    public static final String GET_FIXED_TERM_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.get-fixed-term-account-by-id}";
    public static final String GET_PERSONAL_CHECKING_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.get-personal-checking-account-by-id}";
    public static final String GET_BUSINESS_CHECKING_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.get-business-checking-account-by-id}";
    public static final String UPDATE_SAVING_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.update-saving-account-by-id}";
    public static final String UPDATE_FIXED_TERM_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.update-fixed-term-account-by-id}";
    public static final String UPDATE_CHECKING_ACCOUNT_BY_ID_METHOD = "${bank-account-service.method.update-checking-account-by-id}";

}
