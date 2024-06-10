package com.app.service.currency;

import com.app.entity.CurrencyEntity;
import com.app.models.currency.CurrencyCreationData;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyService {

    List<CurrencyEntity> getAllCurrencies() throws SQLException;
    CurrencyEntity getByCurrencyCode(String currencyCode) throws SQLException;
    CurrencyEntity createCurrency(CurrencyCreationData creationData) throws SQLException;
}
