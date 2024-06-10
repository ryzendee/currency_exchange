package com.app.mapper.currency;

import com.app.entity.CurrencyEntity;
import com.app.mapper.Mapper;
import com.app.models.currency.CurrencyCreationData;

@org.mapstruct.Mapper
public interface CurrencyCreationDataToEntityMapper extends Mapper<CurrencyCreationData, CurrencyEntity> {
}
