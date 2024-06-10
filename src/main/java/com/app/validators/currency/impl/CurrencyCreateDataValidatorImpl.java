package com.app.validators.currency.impl;

import com.app.models.currency.CurrencyCreationData;
import com.app.models.validation.ValidationError;
import com.app.models.validation.ValidationResult;
import com.app.validators.currency.CurrencyCreateDataValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CurrencyCreateDataValidatorImpl implements CurrencyCreateDataValidator {

    private static final String INVALID_NAME = "Name must not be null or empty.";
    private static final String INVALID_CODE = "Code must not be null or empty.";
    private static final String INVALID_SIGN = "Sign must not be null or empty.";

    private static final CurrencyCreateDataValidatorImpl INSTANCE = new CurrencyCreateDataValidatorImpl();

    public static CurrencyCreateDataValidatorImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(CurrencyCreationData currencyCreationData) {
        List<ValidationError> errorList = new ArrayList<>();

        if (StringUtils.isBlank(currencyCreationData.name())) {
            errorList.add(new ValidationError(INVALID_NAME));
        }

        if (StringUtils.isBlank(currencyCreationData.code())) {
            errorList.add(new ValidationError(INVALID_CODE));
        }

        if (StringUtils.isBlank(currencyCreationData.sign())) {
            errorList.add(new ValidationError(INVALID_SIGN));
        }

        return new ValidationResult(errorList);
    }


}
