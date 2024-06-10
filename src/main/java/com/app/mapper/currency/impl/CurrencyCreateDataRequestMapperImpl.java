package com.app.mapper.currency.impl;

import com.app.exception.RequestMappingException;
import com.app.mapper.currency.CurrencyRequestMapper;
import com.app.models.currency.CurrencyCreationData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class CurrencyCreateDataRequestMapperImpl implements CurrencyRequestMapper  {

    private static final CurrencyCreateDataRequestMapperImpl INSTANCE = new CurrencyCreateDataRequestMapperImpl();
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String SIGN = "sign";

    public static CurrencyCreateDataRequestMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public CurrencyCreationData mapFromRequest(HttpServletRequest req) throws RequestMappingException{
        if (hasAnyMissingField(req)) {
            throw new RequestMappingException("Invalid input parameters. You must input name, code and sign");
        }

        return new CurrencyCreationData(
                req.getParameter(NAME),
                req.getParameter(CODE),
                req.getParameter(SIGN)
        );
    }

    private boolean hasAnyMissingField(HttpServletRequest req) {
        return StringUtils.isAnyBlank(
                req.getParameter(NAME),
                req.getParameter(CODE),
                req.getParameter(SIGN)
        );
    }


}
