package com.nttdata.movementservice.config;

import com.nttdata.movementservice.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value(Constants.BANK_ACCOUNT_SERVICE_BASE_URL)
    public String bankAccountServiceBaseUrl;

    @Value(Constants.GET_SAVING_ACCOUNT_BY_ID_METHOD)
    public String getSavingAccountByIdMethod;

    @Value(Constants.GET_FIXED_TERM_ACCOUNT_BY_ID_METHOD)
    public String getFixedTermAccountByIdMethod;

    @Value(Constants.GET_CHECKING_ACCOUNT_BY_ID_METHOD)
    public String getCheckingAccountByIdMethod;

    @Value(Constants.UPDATE_SAVING_ACCOUNT_BY_ID_METHOD)
    public String updateSavingAccountByIdMethod;

    @Value(Constants.UPDATE_FIXED_TERM_ACCOUNT_BY_ID_METHOD)
    public String updateFixedTermAccountByIdMethod;

    @Value(Constants.UPDATE_CHECKING_ACCOUNT_BY_ID_METHOD)
    public String updateCheckingAccountByIdMethod;

    @Value(Constants.CREDIT_SERVICE_BASE_URL)
    public String creditServiceBaseUrl;

    @Value(Constants.GET_CREDIT_CARD_BY_ID_METHOD)
    public String getCreditCardByIdMethod;

    @Value(Constants.UPDATE_CREDIT_CARD_BY_ID_METHOD)
    public String updateCreditCardByIdMethod;

}
