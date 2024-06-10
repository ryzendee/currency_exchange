package com.app.models.exchange;

import java.math.BigDecimal;

public record ExchangeConvertData(
        String baseCurrency,
        String targetCurrency,
        BigDecimal amount
) {
}
