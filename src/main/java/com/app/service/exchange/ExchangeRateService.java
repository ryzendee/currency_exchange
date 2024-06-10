package com.app.service.exchange;

import com.app.entity.ExchangeRateEntity;
import com.app.models.exchange.ExchangeRateCreateData;
import com.app.models.exchange.ExchangeRateUpdateData;
import com.app.models.exchange.ExchangeConvertData;
import com.app.models.exchange.ExchangeResult;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRateEntity> getAllExchanges() throws SQLException;
    ExchangeRateEntity getExchangeRateForCurrencyPair(String baseCurrencyCode, String targetCurrencyCode) throws SQLException;
    ExchangeRateEntity createExchangeRate(ExchangeRateCreateData creationData) throws SQLException;
    ExchangeRateEntity updateExchangeRateCurrencyPair(ExchangeRateUpdateData updateDtoRequest) throws SQLException;
    ExchangeResult convertCurrency(ExchangeConvertData exchangeConvertData) throws SQLException;
}
