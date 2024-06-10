package com.app.mapper.exchange.impl;

import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeCreateRequestMapper;
import com.app.models.exchange.ExchangeRateCreateData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ExchangeRateCreateRequestMapper implements ExchangeCreateRequestMapper {
    private static final ExchangeRateCreateRequestMapper INSTANCE = new ExchangeRateCreateRequestMapper();
    private static final String BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String RATE = "rate";

    public static ExchangeRateCreateRequestMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateCreateData mapFromRequest(HttpServletRequest req) throws RequestMappingException {
        if (hasAnyMissingField(req)) {
            throw new RequestMappingException("Invalid input parameters. You must input base and target currencies codes and their rate");
        }

        String rate = req.getParameter(RATE);
        if (!isNumeric(rate)) {
            throw new RequestMappingException("Rate must be a number.");
        }

        return new ExchangeRateCreateData(
                req.getParameter(BASE_CURRENCY_CODE),
                req.getParameter(TARGET_CURRENCY_CODE),
                new BigDecimal(rate)
        );
    }

    private boolean hasAnyMissingField(HttpServletRequest req) {
        return StringUtils.isAnyBlank(
                req.getParameter(BASE_CURRENCY_CODE),
                req.getParameter(TARGET_CURRENCY_CODE),
                req.getParameter(RATE)
        );
    }

    private boolean isNumeric(String rate) {
        try {
            Integer.parseInt(rate);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
