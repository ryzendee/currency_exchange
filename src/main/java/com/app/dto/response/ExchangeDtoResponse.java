package com.app.dto.response;

import java.math.BigDecimal;

public record ExchangeDtoResponse(
        CurrencyDtoResponse baseDtoCurrency,
        CurrencyDtoResponse targetDtoCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
){

}
