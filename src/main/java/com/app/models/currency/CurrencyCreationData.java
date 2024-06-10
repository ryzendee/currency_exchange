package com.app.models.currency;

public record CurrencyCreationData(
        String name,
        String code,
        String sign
) {
}
