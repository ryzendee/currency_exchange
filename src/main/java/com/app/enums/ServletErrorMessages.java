package com.app.enums;

import org.apache.http.HttpStatus;

public enum ServletErrorMessages {
    //Base
    INTERNAL_SERVER_ERROR("Something went wrong...", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    INVALID_PATH_VARIABLE_ERROR("Invalid path parameters", HttpStatus.SC_BAD_REQUEST),
    VALIDATION_ERROR("This object is invalid", HttpStatus.SC_BAD_REQUEST),
    MAPPING_ERROR("Failed to map object", HttpStatus.SC_BAD_REQUEST),

    //Currency
    CURRENCY_REQUEST_MAPPING_ERROR("Failed to map object", HttpStatus.SC_BAD_REQUEST),
    CURRENCY_EXISTS_ERROR("This currency already exists", HttpStatus.SC_BAD_REQUEST),
    CURRENCY_NOT_FOUND_ERROR("Currency with this code not found", HttpStatus.SC_NOT_FOUND),

    //Exchange Rate,
    EX_RATE_REQUEST_MAPPING_ERROR("Failed to map to object", HttpStatus.SC_BAD_REQUEST),
    EX_RATE_EXISTS_ERROR("This exchange rate is already exists", HttpStatus.SC_CONFLICT),
    EX_RATE_NOT_FOUND_ERROR("Cannot find exchange rate with this parameters", HttpStatus.SC_NOT_FOUND),
    EX_RATE_UPDATE_ERROR("Failed to update exchange rate", HttpStatus.SC_NOT_FOUND);

    private final String message;
    private final int responseCode;
    ServletErrorMessages(String message, int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
