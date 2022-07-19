package com.nttdata.movementservice.config;

import com.nttdata.movementservice.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value(Constants.BANK_ACCOUNT_SERVICE_BASE_URL)
    public String bankAccountServiceBaseUrl;

    @Value(Constants.GET_BANK_ACCOUNT_BY_ID_METHOD)
    public String getBankAccountByIdMethod;

    @Value(Constants.GET_DEBIT_CARD_BY_ID_METHOD)
    public String getDebitCardByIdMethod;

    @Value(Constants.GET_BANK_ACCOUNTS_BY_CUSTOMER_ID_METHOD)
    public String getBankAccountsByCustomerIdMethod;

    @Value(Constants.UPDATE_BANK_ACCOUNT_BY_ID_METHOD)
    public String updateBankAccountByIdMethod;

    @Value(Constants.CREDIT_SERVICE_BASE_URL)
    public String creditServiceBaseUrl;

    @Value(Constants.GET_CREDIT_BY_ID_METHOD)
    public String getCreditByIdMethod;

    @Value(Constants.UPDATE_CREDIT_BY_ID_METHOD)
    public String updateCreditByIdMethod;

}
