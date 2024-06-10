package com.app.mapper.exchange.impl;

import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeConvertDataRequestMapper;
import com.app.models.exchange.ExchangeConvertData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ExchangeConvertDataRequestMapperImpl implements ExchangeConvertDataRequestMapper {

    private static final ExchangeConvertDataRequestMapperImpl INSTANCE = new ExchangeConvertDataRequestMapperImpl();
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "to";
    private static final String AMOUNT_PARAM = "amount";


    public static ExchangeConvertDataRequestMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeConvertData mapFromRequest(HttpServletRequest req) throws RequestMappingException {
        if (hasAnyMissingField(req)) {
            throw new RequestMappingException("Invalid input parameters. You must input from, to and amount params");
        }

        String amount = req.getParameter(AMOUNT_PARAM);
        if (!isNumeric(amount)) {
            throw new RequestMappingException("Amount parameters must be a number");
        }

        return new ExchangeConvertData(
                req.getParameter(FROM_PARAM),
                req.getParameter(TO_PARAM),
                new BigDecimal(amount)
        );

    }

    private boolean hasAnyMissingField(HttpServletRequest req) {
        return StringUtils.isAnyBlank(
                req.getParameter(FROM_PARAM),
                req.getParameter(TO_PARAM),
                req.getParameter(AMOUNT_PARAM)
        );
    }

    private boolean isNumeric(String amount) {
        try {
            Integer.parseInt(amount);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
