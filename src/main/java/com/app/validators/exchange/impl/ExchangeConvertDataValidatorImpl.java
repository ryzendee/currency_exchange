package com.app.validators.exchange.impl;

import com.app.models.exchange.ExchangeConvertData;
import com.app.models.validation.ValidationError;
import com.app.models.validation.ValidationResult;
import com.app.validators.exchange.ExchangeConvertDataValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExchangeConvertDataValidatorImpl implements ExchangeConvertDataValidator {

    private static final ExchangeConvertDataValidatorImpl INSTANCE = new ExchangeConvertDataValidatorImpl();
    private static final String INVALID_BASE_CODE = "Base currency code must not be null or blank";
    private static final String INVALID_TARGET_CODE = "Target currency code must not be null or blank";

    public static ExchangeConvertDataValidatorImpl getInstance() {
        return INSTANCE;
    }
    @Override
    public ValidationResult isValid(ExchangeConvertData object) {
        List<ValidationError> validationErrorList  = new ArrayList<>();

        if (StringUtils.isBlank(object.baseCurrency())) {
            validationErrorList.add(new ValidationError(INVALID_BASE_CODE));
        }

        if (StringUtils.isBlank(object.targetCurrency())) {
            validationErrorList.add(new ValidationError(INVALID_TARGET_CODE));
        }

        return new ValidationResult(validationErrorList);
    }
}
