package com.app.validators.exchange.impl;

import com.app.models.exchange.ExchangeRateCreateData;
import com.app.models.validation.ValidationError;
import com.app.models.validation.ValidationResult;
import com.app.validators.exchange.ExchangeRateCreateDataValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateCreateDataValidatorImpl implements ExchangeRateCreateDataValidator {
    private static final String INVALID_BASE_CODE = "Base currency code must not be null or blank";
    private static final String INVALID_TARGET_CODE = "Target currency code must not be null or blank";
    private static final ExchangeRateCreateDataValidatorImpl INSTANCE = new ExchangeRateCreateDataValidatorImpl();

    public static ExchangeRateCreateDataValidatorImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(ExchangeRateCreateData object) {
        List<ValidationError> validationErrorList  = new ArrayList<>();

        if (StringUtils.isBlank(object.baseCurrencyCode())) {
            validationErrorList.add(new ValidationError(INVALID_BASE_CODE));
        }

        if (StringUtils.isBlank(object.targetCurrencyCode())) {
            validationErrorList.add(new ValidationError(INVALID_TARGET_CODE));
        }

        return new ValidationResult(validationErrorList);
    }
}
