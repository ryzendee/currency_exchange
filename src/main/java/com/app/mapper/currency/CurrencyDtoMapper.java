package com.app.mapper.currency;

import com.app.dto.response.CurrencyDtoResponse;
import com.app.entity.CurrencyEntity;
import com.app.mapper.Mapper;

@org.mapstruct.Mapper
public interface CurrencyDtoMapper extends Mapper<CurrencyEntity, CurrencyDtoResponse> {

}
