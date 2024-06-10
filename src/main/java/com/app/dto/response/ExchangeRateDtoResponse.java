package com.app.dto.response;

import java.math.BigDecimal;

public record ExchangeRateDtoResponse (
        Integer id,
        CurrencyDtoResponse baseCurrency,
        CurrencyDtoResponse targetCurrency,
        BigDecimal rate
) {
}
