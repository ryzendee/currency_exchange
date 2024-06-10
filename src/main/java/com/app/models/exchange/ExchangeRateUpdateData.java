package com.app.models.exchange;

import java.math.BigDecimal;

public record ExchangeRateUpdateData(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate
) {
}
