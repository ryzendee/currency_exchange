package com.app.dao.exchangerate;

import com.app.entity.ExchangeRateEntity;
import com.app.mapper.exchange.impl.ExchangeRateRowMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDaoImpl();
    private final ExchangeRateRowMapperImpl exchangeRateRowMapper = ExchangeRateRowMapperImpl.getInstance();

    //SELECT STATEMENTS
    private static final String SELECT_ALL_SQL = """
            SELECT
                exchange_rates.id,
                exchange_rates.base_currency_id,
                exchange_rates.target_currency_id,
                exchange_rates.rate,
                base_currency.id as base_id,
                base_currency.full_name as base_full_name,
                base_currency.sign as base_sign,
                base_currency.code as base_code,
                target_currency.id as target_id,
                target_currency.full_name as target_full_name,
                target_currency.sign as target_sign,
                target_currency.code as target_code
            FROM exchange_rates
                JOIN currencies base_currency
                    ON exchange_rates.base_currency_id = base_currency.id
                JOIN currencies target_currency
                    ON exchange_rates.target_currency_id = target_currency.id
            """;
    private static final String SELECT_BY_BASE_AND_TARGET_CURRENCY_CODE_SQL = SELECT_ALL_SQL + """
            WHERE base_currency.code = ?
                AND target_currency.code = ?
            """;

    private static final String SELECT_ALL_BY_BASE_AND_TARGET_CODES_IN = SELECT_ALL_SQL + """
            WHERE base_currency.code = ?
                AND target_currency.code IN (?, ?)
            """;

    //INSERT STATEMENTS
    private static final String INSERT_SQL = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
            VALUES(?, ?, ?)
            """;

    //UPDATE STATEMENTS
    private static final String UPDATE_BY_ID_SQL = """
            UPDATE exchange_rates
                SET
                    (base_currency_id, target_currency_id, rate) = (?, ?, ?)
                WHERE exchange_rates.id = ?
            """;

    private ExchangeRateDaoImpl() {

    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity entity, Connection connection) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setInt(1, entity.getBaseCurrency().getId());
            insertStatement.setInt(2, entity.getTargetCurrency().getId());
            insertStatement.setBigDecimal(3, entity.getRate());
            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            entity.setId(rs.getInt(1));

            return entity;
        }
    }

    @Override
    public List<ExchangeRateEntity> findAll(Connection connection) throws SQLException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_ALL_SQL)) {
            List<ExchangeRateEntity> exchangeRateEntityList = new ArrayList<>();
            ResultSet rs = selectStatement.executeQuery();

            return processResultSet(exchangeRateEntityList, rs);
        }
    }

    private List<ExchangeRateEntity> processResultSet(List<ExchangeRateEntity> exchangeRateEntityList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            ExchangeRateEntity entity = exchangeRateRowMapper.map(rs);
            exchangeRateEntityList.add(entity);
        }

        return exchangeRateEntityList;
    }

    @Override
    public boolean updateById(Integer id, ExchangeRateEntity updatedEntity, Connection connection) throws SQLException {
        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_BY_ID_SQL)) {

            updateStatement.setInt(1, updatedEntity.getBaseCurrency().getId());
            updateStatement.setInt(2, updatedEntity.getTargetCurrency().getId());
            updateStatement.setBigDecimal(3, updatedEntity.getRate());
            updateStatement.setInt(4, updatedEntity.getId());

            return updateStatement.executeUpdate() > 0;
        }
    }


    @Override
    public Optional<ExchangeRateEntity> findByBaseCurrencyCodeAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws SQLException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_BY_BASE_AND_TARGET_CURRENCY_CODE_SQL)) {

            selectStatement.setString(1, baseCurrencyCode);
            selectStatement.setString(2, targetCurrencyCode);

            ResultSet rs = selectStatement.executeQuery();
            ExchangeRateEntity entity = null;
            if (rs.next()) {
                entity = exchangeRateRowMapper.map(rs);
            }

            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<ExchangeRateEntity> findAllByBaseCurrencyAndTargetCodesIn(String baseCurrencyCode, String firstTargetCode, String secondTargetCode, Connection connection) throws SQLException {
        try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_ALL_BY_BASE_AND_TARGET_CODES_IN)) {

            selectStatement.setString(1, baseCurrencyCode);
            selectStatement.setString(2, firstTargetCode);
            selectStatement.setString(3, secondTargetCode);

            ResultSet rs = selectStatement.executeQuery();
            List<ExchangeRateEntity> exchangeRateEntityList = new ArrayList<>();
            return processResultSet(exchangeRateEntityList, rs);
        }
    }
}
