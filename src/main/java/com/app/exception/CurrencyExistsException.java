package com.app.exception;

public class CurrencyExistsException extends RuntimeException {

    public CurrencyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
