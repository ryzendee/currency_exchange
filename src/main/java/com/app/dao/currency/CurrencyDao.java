package com.app.dao.currency;

import com.app.dao.BaseDao;
import com.app.entity.CurrencyEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface CurrencyDao extends BaseDao<Integer, CurrencyEntity> {

    Optional<CurrencyEntity> findByCurrencyCode(String currencyCode, Connection connection) throws SQLException;
}
