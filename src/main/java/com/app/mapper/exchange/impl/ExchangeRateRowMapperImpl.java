package com.app.mapper.exchange.impl;

import com.app.entity.CurrencyEntity;
import com.app.entity.ExchangeRateEntity;
import com.app.mapper.currency.CurrencyRowMapper;
import com.app.mapper.currency.impl.CurrencyRowMapperImpl;
import com.app.mapper.exchange.ExchangeRateRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateRowMapperImpl implements ExchangeRateRowMapper {
    private static final ExchangeRateRowMapperImpl INSTANCE = new ExchangeRateRowMapperImpl();
    private static final String ID = "id";
    private static final String RATE = "rate";

    private final CurrencyRowMapper currencyRowMapper = CurrencyRowMapperImpl.getInstance();

    public static ExchangeRateRowMapperImpl getInstance() {
        return INSTANCE;
    }
    @Override
    public ExchangeRateEntity map(ResultSet rs) throws SQLException {
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
        exchangeRateEntity.setId(rs.getInt(ID));
        exchangeRateEntity.setRate(rs.getBigDecimal(RATE));

        CurrencyEntity baseCurrency = currencyRowMapper.mapBaseCurrency(rs);
        CurrencyEntity targetCurrency = currencyRowMapper.mapTargetCurrency(rs);
        exchangeRateEntity.setBaseCurrency(baseCurrency);
        exchangeRateEntity.setTargetCurrency(targetCurrency);

        return exchangeRateEntity;
    }

}
