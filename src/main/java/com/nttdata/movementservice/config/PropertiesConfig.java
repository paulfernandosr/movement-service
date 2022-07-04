package com.nttdata.movementservice.config;

import com.nttdata.movementservice.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Value(Constants.BANK_ACCOUNT_SERVICE_BASE_URL)
    public String bankAccountsServiceBaseUrl;

    @Value(Constants.GET_SAVING_ACCOUNT_BY_ID_METHOD)
    public String getSavingAccountByIdMethod;

    @Value(Constants.GET_FIXED_TERM_ACCOUNT_BY_ID_METHOD)
    public String getFixedTermAccountByIdMethod;

    @Value(Constants.GET_PERSONAL_CHECKING_ACCOUNT_BY_ID_METHOD)
    public String getPersonalCheckingAccountByIdMethod;

    @Value(Constants.GET_BUSINESS_CHECKING_ACCOUNT_BY_ID_METHOD)
    public String getBusinessCheckingAccountByIdMethod;

    @Value(Constants.UPDATE_SAVING_ACCOUNT_BY_ID_METHOD)
    public String updateSavingAccountByIdMethod;

    @Value(Constants.UPDATE_FIXED_TERM_ACCOUNT_BY_ID_METHOD)
    public String updateFixedTermAccountByIdMethod;

    @Value(Constants.UPDATE_CHECKING_ACCOUNT_BY_ID_METHOD)
    public String updateCheckingAccountByIdMethod;

}
