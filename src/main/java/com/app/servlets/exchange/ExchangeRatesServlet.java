package com.app.servlets.exchange;


import com.app.dto.response.ExchangeRateDtoResponse;
import com.app.entity.ExchangeRateEntity;
import com.app.exception.CurrencyNotFoundException;
import com.app.exception.ExchangeRateExistsException;
import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeRateEntityToDtoMapper;
import com.app.mapper.exchange.impl.ExchangeRateRateCreateRequestMapperImpl;
import com.app.models.exchange.ExchangeRateCreateData;
import com.app.models.validation.ValidationResult;
import com.app.service.exchange.ExchangeRateService;
import com.app.service.exchange.ExchangeRateServiceImpl;
import com.app.validators.exchange.ExchangeRateCreateDataValidator;
import com.app.validators.exchange.impl.ExchangeRateCreateDataValidatorImpl;
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

import static com.app.enums.ServletErrorMessages.*;
import static com.app.utils.ServletWriter.*;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet  {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServlet.class);
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final ExchangeRateEntityToDtoMapper exchangeRateEntityToDtoMapper = Mappers.getMapper(ExchangeRateEntityToDtoMapper.class);
    private final ExchangeRateRateCreateRequestMapperImpl servletRequestMapper = ExchangeRateRateCreateRequestMapperImpl.getInstance();
    private final ExchangeRateCreateDataValidator creationDataValidator = ExchangeRateCreateDataValidatorImpl.getInstance();

    //Get all exchange rates
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateDtoResponse> dtoResponseList = exchangeRateService.getAllExchanges().stream()
                    .map(exchangeRateEntityToDtoMapper::map)
                    .toList();
            mapToJsonAndWriteInResponse(dtoResponseList, resp);
        } catch (SQLException ex) {
            LOG.error("Database problem", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }

    //Create new exchange rate
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //Extracts params from request, if field is missing throws RequestMappingEx
            ExchangeRateCreateData creationData = servletRequestMapper.mapFromRequest(req);

            //Checks fields for null or blank
            ValidationResult validationResult = creationDataValidator.isValid(creationData);
            if (validationResult.hasAnyErrors()) {
                writeInvalidResponse(validationResult, resp);
            }

            ExchangeRateEntity createdExchangeRate = exchangeRateService.createExchangeRate(creationData);
            ExchangeRateDtoResponse mappedExchangeRate = exchangeRateEntityToDtoMapper.map(createdExchangeRate);

            mapToJsonAndWriteInResponse(mappedExchangeRate, resp);
        } catch (RequestMappingException ex) {
            writeErrorResponse(ex, resp, MAPPING_ERROR);
        } catch (CurrencyNotFoundException ex) {
            LOG.warn("Currency not found", ex);
            writeErrorResponse(CURRENCY_NOT_FOUND_ERROR, resp);
        } catch (ExchangeRateExistsException ex) {
            LOG.warn("Ex rate exists", ex);
            writeErrorResponse(EX_RATE_EXISTS_ERROR, resp);
        } catch (SQLException ex) {
            LOG.error("Database problem", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }

}
