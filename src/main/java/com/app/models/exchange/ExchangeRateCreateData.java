package com.app.models.exchange;

import java.math.BigDecimal;

public record ExchangeRateCreateData(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate
) {
}
