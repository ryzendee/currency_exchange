package com.app.exception;

public class ExchangeRateExistsException extends RuntimeException {
    public ExchangeRateExistsException(String message) {
        super(message);
    }

    public ExchangeRateExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
