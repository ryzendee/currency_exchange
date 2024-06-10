package com.app.mapper.currency.impl;

import com.app.entity.CurrencyEntity;
import com.app.mapper.currency.CurrencyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRowMapperImpl implements CurrencyRowMapper {

    private static final CurrencyRowMapperImpl INSTANCE = new CurrencyRowMapperImpl();
    private static final String ID = "id";
    private static final String FULL_NAME = "full_name";
    private static final String SIGN = "sign";
    private static final String CODE = "code";
    private static final String BASE_PREFIX = "base_";
    private static final String TARGET_PREFIX = "target_";

    public static CurrencyRowMapperImpl getInstance() {
        return INSTANCE;
    }

    private CurrencyRowMapperImpl() {

    }
    @Override
    public CurrencyEntity map(ResultSet rs) throws SQLException {
        return new CurrencyEntity(
                rs.getInt(ID),
                rs.getString(FULL_NAME),
                rs.getString(CODE),
                rs.getString(SIGN)
        );
    }

    @Override
    public CurrencyEntity mapBaseCurrency(ResultSet rs) throws SQLException {
        return mapCurrency(rs, BASE_PREFIX);
    }

    @Override
    public CurrencyEntity mapTargetCurrency(ResultSet rs) throws SQLException {
        return mapCurrency(rs, TARGET_PREFIX);
    }

    private CurrencyEntity mapCurrency(ResultSet rs, String prefix) throws SQLException {
        return new CurrencyEntity(
                rs.getInt(prefix + ID),
                rs.getString(prefix + FULL_NAME),
                rs.getString(prefix + CODE),
                rs.getString(prefix + SIGN)
        );
    }
}
