package com.app.mapper.currency;

import com.app.entity.CurrencyEntity;
import com.app.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CurrencyRowMapper extends RowMapper<CurrencyEntity> {

    CurrencyEntity mapBaseCurrency(ResultSet rs) throws SQLException;
    CurrencyEntity mapTargetCurrency(ResultSet rs) throws SQLException;
}
