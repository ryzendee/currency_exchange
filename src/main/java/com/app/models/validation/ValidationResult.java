package com.app.models.validation;

import java.util.List;

public record ValidationResult(
        List<ValidationError> validationErrorsList
) {

    public boolean hasAnyErrors() {
        return !validationErrorsList.isEmpty();
    }
}
