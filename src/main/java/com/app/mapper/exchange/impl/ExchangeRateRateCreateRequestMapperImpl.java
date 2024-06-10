package com.app.mapper.exchange.impl;

import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeRateCreateRequestMapper;
import com.app.models.exchange.ExchangeRateCreateData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ExchangeRateRateCreateRequestMapperImpl implements ExchangeRateCreateRequestMapper {
    private static final ExchangeRateRateCreateRequestMapperImpl INSTANCE = new ExchangeRateRateCreateRequestMapperImpl();
    private static final String BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String RATE = "rate";

    public static ExchangeRateRateCreateRequestMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateCreateData mapFromRequest(HttpServletRequest req) throws RequestMappingException {
        if (hasAnyMissingField(req)) {
            throw new RequestMappingException("Invalid input parameters. You must input base and target currencies codes and their rate");
        }

        BigDecimal rate = getRate(req.getParameter(RATE));

        return new ExchangeRateCreateData(
                req.getParameter(BASE_CURRENCY_CODE),
                req.getParameter(TARGET_CURRENCY_CODE),
                rate
        );
    }

    private boolean hasAnyMissingField(HttpServletRequest req) {
        return StringUtils.isAnyBlank(
                req.getParameter(BASE_CURRENCY_CODE),
                req.getParameter(TARGET_CURRENCY_CODE),
                req.getParameter(RATE)
        );
    }

    private BigDecimal getRate(String rateParam) {
        try {
            return new BigDecimal(rateParam);
        } catch (NumberFormatException ex) {
            throw new RequestMappingException("Rate must be a number");
        }
    }
}
