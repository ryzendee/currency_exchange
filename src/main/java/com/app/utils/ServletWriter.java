package com.app.utils;

import com.app.dto.response.ErrorResponse;
import com.app.enums.ServletErrorMessages;
import com.app.models.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public final class ServletWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static void writeErrorResponse(ServletErrorMessages errorMessages, HttpServletResponse httpServletResponse) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(errorMessages);
        httpServletResponse.setStatus(errorMessages.getResponseCode());
        mapToJsonAndWriteInResponse(errorResponse, httpServletResponse);
    }

    public static void writeErrorResponse(Exception ex, HttpServletResponse resp, ServletErrorMessages servletErrorMessages) throws IOException {
        String errorMessage = servletErrorMessages.getMessage() + " : " + ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, servletErrorMessages.getResponseCode());
        resp.setStatus(servletErrorMessages.getResponseCode());
        mapToJsonAndWriteInResponse(errorResponse, resp);
    }

    public static void writeInvalidResponse(ValidationResult validationResult, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(ServletErrorMessages.VALIDATION_ERROR.getResponseCode());
        mapToJsonAndWriteInResponse(validationResult, httpServletResponse);
    }

    public static void mapToJsonAndWriteInResponse(Object object, HttpServletResponse response) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        OBJECT_MAPPER.writeValue(response.getWriter(), object);
    }}
