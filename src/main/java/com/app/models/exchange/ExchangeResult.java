package com.app.models.exchange;

import com.app.entity.ExchangeRateEntity;

import java.math.BigDecimal;

public record ExchangeResult (
        ExchangeRateEntity exchangeRate,
        BigDecimal amount,
        BigDecimal convertedAmount
){
}
