package com.app.models.exchange;

import com.app.dto.response.CurrencyDtoResponse;
import com.app.entity.CurrencyEntity;
import com.app.entity.ExchangeRateEntity;

import java.math.BigDecimal;

public record ExchangeResult (
        ExchangeRateEntity exchangeRate,
        BigDecimal amount,
        BigDecimal convertedAmount
){
}
