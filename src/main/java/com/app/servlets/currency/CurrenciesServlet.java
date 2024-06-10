package com.app.servlets.currency;

import com.app.dto.response.CurrencyDtoResponse;
import com.app.entity.CurrencyEntity;
import com.app.enums.ServletErrorMessages;
import com.app.exception.CurrencyExistsException;
import com.app.exception.RequestMappingException;
import com.app.mapper.ServletRequestMapper;
import com.app.mapper.currency.CurrencyDtoMapper;
import com.app.mapper.currency.impl.CurrencyCreateDataRequestMapperImpl;
import com.app.models.currency.CurrencyCreationData;
import com.app.models.validation.ValidationResult;
import com.app.service.currency.CurrencyService;
import com.app.service.currency.CurrencyServiceImpl;
import com.app.validators.currency.CurrencyCreateDataValidator;
import com.app.validators.currency.impl.CurrencyCreateDataValidatorImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.app.enums.ServletErrorMessages.CURRENCY_EXISTS_ERROR;
import static com.app.enums.ServletErrorMessages.INTERNAL_SERVER_ERROR;
import static com.app.utils.ServletWriter.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(CurrenciesServlet.class);

    private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
    private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
    private final ServletRequestMapper<CurrencyCreationData> servletRequestMapper = CurrencyCreateDataRequestMapperImpl.getInstance();
    private final CurrencyCreateDataValidator validator = CurrencyCreateDataValidatorImpl.getInstance();

    //Get all currencies
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDtoResponse> currencyDtoList = currencyService.getAllCurrencies().stream()
                    .map(currencyDtoMapper::map)
                    .toList();
            mapToJsonAndWriteInResponse(currencyDtoList, resp);
        } catch (SQLException ex) {
            LOG.error("Database problem", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }

    //Create new currency from createDtoRequest
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //Extracts params from request, if field is missing throws RequestMappingEx
            CurrencyCreationData creationData = servletRequestMapper.mapFromRequest(req);

            //Checks fields for null or blank
            ValidationResult validationResult = validator.isValid(creationData);
            if (validationResult.hasAnyErrors()) {
                LOG.warn("Invalid currency creation data: {}", validationResult);
                writeInvalidResponse(validationResult, resp);
                return;
            }

            CurrencyEntity createdEntity = currencyService.createCurrency(creationData);
            CurrencyDtoResponse dtoResponse = currencyDtoMapper.map(createdEntity);

            mapToJsonAndWriteInResponse(dtoResponse, resp);
        } catch (RequestMappingException ex) {
            writeErrorResponse(ex, resp, ServletErrorMessages.MAPPING_ERROR);
        } catch (CurrencyExistsException ex) {
            LOG.warn("Currency exists exception", ex);
            writeErrorResponse(CURRENCY_EXISTS_ERROR, resp);
        } catch (SQLException ex) {
            LOG.error("Problems with database", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }

}
