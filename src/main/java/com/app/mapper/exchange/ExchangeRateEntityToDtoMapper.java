package com.app.mapper.exchange;

import com.app.dto.response.ExchangeRateDtoResponse;
import com.app.entity.ExchangeRateEntity;
import com.app.mapper.Mapper;

@org.mapstruct.Mapper
public interface ExchangeRateEntityToDtoMapper extends Mapper<ExchangeRateEntity, ExchangeRateDtoResponse> {

}
