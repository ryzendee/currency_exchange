package com.app.mapper.exchange;

import com.app.dto.response.ExchangeDtoResponse;
import com.app.mapper.Mapper;
import com.app.models.exchange.ExchangeResult;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper
public interface ExchangeResultToDtoMapper extends Mapper<ExchangeResult, ExchangeDtoResponse> {

    @Mapping(source = "exchangeRate.baseCurrency", target = "baseDtoCurrency")
    @Mapping(source = "exchangeRate.targetCurrency", target = "targetDtoCurrency")
    @Mapping(source = "exchangeRate.rate", target = "rate")
    ExchangeDtoResponse map(ExchangeResult from);
}
