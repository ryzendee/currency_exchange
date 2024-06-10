package com.app.dao.exchangerate;

import com.app.dao.BaseDao;
import com.app.entity.ExchangeRateEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao extends BaseDao<Long, ExchangeRateEntity> {

    ExchangeRateEntity save(ExchangeRateEntity entity, Connection connection) throws SQLException;
    boolean updateById(Integer id, ExchangeRateEntity updatedEntity, Connection connection) throws SQLException;
    Optional<ExchangeRateEntity> findByBaseCurrencyCodeAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws SQLException;
    List<ExchangeRateEntity> findAllByBaseCurrencyAndTargetCodesIn(String baseCurrencyCode, String firstTargetCode, String secondTargetCode, Connection connection) throws SQLException;
}
