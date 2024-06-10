package com.app.service.currency;

import com.app.dao.currency.CurrencyDao;
import com.app.dao.currency.CurrencyDaoImpl;
import com.app.entity.CurrencyEntity;
import com.app.exception.CurrencyExistsException;
import com.app.exception.CurrencyNotFoundException;
import com.app.mapper.currency.CurrencyCreationDataToEntityMapper;
import com.app.models.currency.CurrencyCreationData;
import com.app.utils.ConnectionPoolManager;
import org.mapstruct.factory.Mappers;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyServiceImpl();
    private final CurrencyDao currencyDao = CurrencyDaoImpl.getInstance();
    private final CurrencyCreationDataToEntityMapper creationDataToEntityMapper = Mappers.getMapper(CurrencyCreationDataToEntityMapper.class);

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyServiceImpl() {

    }

    @Override
    public List<CurrencyEntity> getAllCurrencies() throws SQLException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {
            return currencyDao.findAll(connection);
        }
    }

    @Override
    public CurrencyEntity getByCurrencyCode(String currencyCode) throws SQLException {
        try (Connection connection = ConnectionPoolManager.getConnection()) {
            return currencyDao.findByCurrencyCode(currencyCode, connection)
                    .orElseThrow(() -> new CurrencyNotFoundException("Currency not found! Code " + currencyCode));
        }
    }

    @Override
    public CurrencyEntity createCurrency(CurrencyCreationData currencyCreationData) throws SQLException, CurrencyExistsException{
        try (Connection connection = ConnectionPoolManager.getConnection()) {
            CurrencyEntity entity = creationDataToEntityMapper.map(currencyCreationData);
            return currencyDao.save(entity, connection);
        } catch (SQLiteException ex) {

            if (isUniqueConstraint(ex)) {
                throw new CurrencyExistsException("Currency with this code already exists", ex);
            }

            throw ex;
        }
    }

    private boolean isUniqueConstraint(SQLiteException ex) {
        return ex.getResultCode().equals(SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE);
    }
}
