package com.app.mapper.exchange.impl;

import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeRateUpdateRequestMapper;
import com.app.models.exchange.ExchangeRateUpdateData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static com.app.utils.PathVariableReader.extractBaseCurrency;
import static com.app.utils.PathVariableReader.extractTargetCurrency;

public class ExchangeRateUpdateRequestMapperImpl implements ExchangeRateUpdateRequestMapper {

    private static final ExchangeRateUpdateRequestMapperImpl INSTANCE = new ExchangeRateUpdateRequestMapperImpl();
    private static final String RATE = "rate=";

    public static ExchangeRateUpdateRequestMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateUpdateData mapFromRequest(HttpServletRequest req) throws RequestMappingException {
        String rateParam = readRate(req);
        BigDecimal rate = getRate(rateParam);
        String baseCurrency = extractBaseCurrency(req.getPathInfo());
        String targetCurrency = extractTargetCurrency(req.getPathInfo());

        return new ExchangeRateUpdateData(baseCurrency, targetCurrency, rate);
    }

    private String readRate(HttpServletRequest req) throws RequestMappingException{
        try {
            String rateParam = req.getReader().readLine();

            if (isRateMissing(rateParam)) {
                throw new RequestMappingException("Invalid input parameters. You should input rate");
            }

            return rateParam.replace(RATE, "");
        } catch (IOException ex) {
            throw new RequestMappingException("Failed to read input param: " + ex.getMessage());
        }
    }

    private boolean isRateMissing(String rateParam) {
        return StringUtils.isBlank(rateParam) || !rateParam.contains(RATE);
    }

    private BigDecimal getRate(String rateParam) {
        try {
            return new BigDecimal(rateParam);
        } catch (NumberFormatException ex) {
            throw new RequestMappingException("Rate must be a number");
        }
    }
}
