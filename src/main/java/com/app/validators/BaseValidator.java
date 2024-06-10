package com.app.validators;

import com.app.models.validation.ValidationResult;

public interface BaseValidator<T> {

    ValidationResult isValid(T object);
}
