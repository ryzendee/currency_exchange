package com.app.dto.response;

import java.math.BigDecimal;

public record ExchangeDtoResponse(
        CurrencyDtoResponse baseCurrency,
        CurrencyDtoResponse targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
){

}
