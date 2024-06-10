package com.app.dao.currency;

import com.app.entity.CurrencyEntity;
import com.app.mapper.currency.CurrencyRowMapper;
import com.app.mapper.currency.impl.CurrencyRowMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDaoImpl();
    private final CurrencyRowMapper currencyRowMapper = CurrencyRowMapperImpl.getInstance();

    //INSERT STATEMENTS
    private static final String SAVE_SQL = """
            INSERT INTO currencies (full_name, code, sign)
            VALUES(?, ?, ?)
            """;

    //SELECT STATEMENTS
    private static final String SELECT_ALL_SQL = """
            SELECT
                currencies.id,
                currencies.full_name,
                currencies.code,
                currencies.sign
            FROM currencies
            """;
    private static final String SELECT_BY_CODE_SQL = SELECT_ALL_SQL + """
            WHERE currencies.code = ?
            """;


    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    public CurrencyEntity save(CurrencyEntity entity, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCode());
            preparedStatement.setString(3, entity.getSign());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            entity.setId(rs.getInt(1));
            return entity;
        }
    }

    @Override
    public List<CurrencyEntity> findAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            List<CurrencyEntity> currencyEntityList = new ArrayList<>();

            while (rs.next()) {
                CurrencyEntity entity = currencyRowMapper.map(rs);
                currencyEntityList.add(entity);
            }

            return currencyEntityList;
        }
    }

    @Override
    public Optional<CurrencyEntity> findByCurrencyCode(String currencyCode, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            statement.setString(1, currencyCode);
            ResultSet rs = statement.executeQuery();

            CurrencyEntity entity = null;
            if (rs.next()) {
                entity = currencyRowMapper.map(rs);
            }

            return Optional.ofNullable(entity);
        }
    }
}
