package com.app.service.exchange;

import com.app.dao.exchangerate.ExchangeRateDao;
import com.app.dao.exchangerate.ExchangeRateDaoImpl;
import com.app.entity.CurrencyEntity;
import com.app.entity.ExchangeRateEntity;
import com.app.exception.CurrencyNotFoundException;
import com.app.exception.ExchangeRateExistsException;
import com.app.exception.ExchangeRateNotFoundException;
import com.app.exception.ExchangeUpdateException;
import com.app.models.exchange.ExchangeConvertData;
import com.app.models.exchange.ExchangeRateCreateData;
import com.app.models.exchange.ExchangeRateUpdateData;
import com.app.models.exchange.ExchangeResult;
import com.app.service.currency.CurrencyService;
import com.app.service.currency.CurrencyServiceImpl;
import com.app.utils.ConnectionPoolManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final ExchangeRateServiceImpl INSTANCE = new ExchangeRateServiceImpl();
    private static final String ROOT_USD_CODE = "USD";
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoImpl.getInstance();
    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();

    public static ExchangeRateServiceImpl getInstance() {
        return INSTANCE;
    }

    private ExchangeRateServiceImpl() {

    }

    @Override
    public List<ExchangeRateEntity> getAllExchanges() throws SQLException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {
            return exchangeRateDao.findAll(connection);
        }
    }

    @Override
    public ExchangeRateEntity createExchangeRate(ExchangeRateCreateData createDtoRequest) throws CurrencyNotFoundException, SQLException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {

            CurrencyEntity baseCurrency = currencyService.getByCurrencyCode(createDtoRequest.baseCurrencyCode());
            CurrencyEntity targetCurrency = currencyService.getByCurrencyCode(createDtoRequest.targetCurrencyCode());

            ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity(baseCurrency, targetCurrency, createDtoRequest.rate());
            return exchangeRateDao.save(exchangeRateEntity, connection);
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new ExchangeRateExistsException("This exchange rate already exists!", ex);
        }
    }

    @Override
    public ExchangeRateEntity getExchangeRateForCurrencyPair(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, ExchangeRateNotFoundException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {
             return getExchangeRateByCurrencyCodes(baseCurrencyCode, targetCurrencyCode, connection);
        }
    }

    @Override
    public ExchangeResult convertCurrency(ExchangeConvertData exchangeConvertData) throws SQLException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {

            ExchangeRateEntity entity = getExchangeRateByCurrencyCodes(exchangeConvertData.baseCurrency(), exchangeConvertData.targetCurrency(), connection);
            BigDecimal convertedAmount = entity.getRate().multiply(exchangeConvertData.amount());

            return new ExchangeResult(
                    entity,
                    exchangeConvertData.amount(),
                    convertedAmount
            );
        }
    }

    private ExchangeRateEntity getExchangeRateByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws ExchangeRateNotFoundException, SQLException {
        //Direct search
        Optional<ExchangeRateEntity> exchangeRateEntityOptional = getByBaseAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode, connection);

        //Inverted search
        if (exchangeRateEntityOptional.isEmpty()) {
            exchangeRateEntityOptional = getByReversedCurrenciesCodes(baseCurrencyCode, targetCurrencyCode, connection);
        }

        //By root-base currency code that we set in constant
        if (exchangeRateEntityOptional.isEmpty()) {
            exchangeRateEntityOptional = getByRootCodeAndCodesIn(baseCurrencyCode, targetCurrencyCode, connection);
        }

        return exchangeRateEntityOptional
                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange not found, because this currencies does not exists!"));
    }


    private Optional<ExchangeRateEntity> getByBaseAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws SQLException {
        return exchangeRateDao.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode, connection);
    }

    private Optional<ExchangeRateEntity> getByReversedCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws SQLException {
        return exchangeRateDao.findByBaseCurrencyCodeAndTargetCurrencyCode(targetCurrencyCode, baseCurrencyCode, connection)
                .map(entity -> new ExchangeRateEntity(entity.getTargetCurrency(), entity.getBaseCurrency(), entity.getRate()))
                .or(Optional::empty);
    }

    private Optional<ExchangeRateEntity> getByRootCodeAndCodesIn(String baseCurrencyCode, String targetCurrencyCode, Connection connection) throws SQLException {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRateDao
                .findAllByBaseCurrencyAndTargetCodesIn(ROOT_USD_CODE, baseCurrencyCode, targetCurrencyCode, connection);

        Optional<ExchangeRateEntity> usdToBaseOptional = findFirstByCode(exchangeRateEntityList, baseCurrencyCode);
        Optional<ExchangeRateEntity> usdToTargetCurrencyOptional = findFirstByCode(exchangeRateEntityList, targetCurrencyCode);

        return usdToBaseOptional.flatMap(usdToBaseEntity ->
                usdToTargetCurrencyOptional.map(usdToTargetEntity -> {
                    BigDecimal updatedRate = updateExchangeRate(usdToBaseEntity.getRate(), usdToTargetEntity.getRate());
                    return new ExchangeRateEntity(
                            usdToBaseEntity.getTargetCurrency(),
                            usdToTargetEntity.getTargetCurrency(),
                            updatedRate);
                })).or(Optional::empty);
    }

    private Optional<ExchangeRateEntity> findFirstByCode(List<ExchangeRateEntity> exchangeRateEntityList, String code) {
        return exchangeRateEntityList.stream()
                .filter(exchangeRate -> exchangeRate.getTargetCurrency().getCode().equals(code))
                .findFirst();
    }

    private BigDecimal updateExchangeRate(BigDecimal baseRate, BigDecimal targetRate) {
        return targetRate.divide(baseRate, MathContext.DECIMAL32);
    }
    @Override
    public ExchangeRateEntity updateExchangeRateCurrencyPair(ExchangeRateUpdateData updateData) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectionPoolManager.getConnection();
            connection.setAutoCommit(false);

            ExchangeRateEntity entityToUpdate = exchangeRateDao.findByBaseCurrencyCodeAndTargetCurrencyCode(
                    updateData.baseCurrencyCode(),
                    updateData.targetCurrencyCode(),
                    connection
            ).map(entity -> {
                entity.setRate(updateData.rate());
                return entity;
            }).orElseThrow(() -> new ExchangeUpdateException("This currency pair does not exists"));

            exchangeRateDao.updateById(entityToUpdate.getId(), entityToUpdate, connection);

            connection.commit();
            connection.setAutoCommit(true);

            return entityToUpdate;
        } catch (SQLException ex) {
            rollbackTransactionAndSetAutoCommitTrue(connection);
            throw ex;
        }
    }

    private void rollbackTransactionAndSetAutoCommitTrue(Connection connection) throws SQLException {
        if (connection != null) {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

}
