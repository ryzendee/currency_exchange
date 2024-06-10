package com.app.dto.response;

import com.app.enums.ServletErrorMessages;

public record ErrorResponse(String message, int statusCode) {

    public ErrorResponse(ServletErrorMessages messages) {
        this(messages.getMessage(), messages.getResponseCode());
    }
}
