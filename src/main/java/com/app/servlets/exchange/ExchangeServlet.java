package com.app.servlets.exchange;

import com.app.dto.response.ExchangeDtoResponse;
import com.app.exception.ExchangeRateNotFoundException;
import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeConvertDataRequestMapper;
import com.app.mapper.exchange.ExchangeResultToDtoMapper;
import com.app.mapper.exchange.impl.ExchangeConvertDataRequestMapperImpl;
import com.app.models.exchange.ExchangeConvertData;
import com.app.models.exchange.ExchangeResult;
import com.app.models.validation.ValidationResult;
import com.app.service.exchange.ExchangeRateService;
import com.app.service.exchange.ExchangeRateServiceImpl;
import com.app.validators.exchange.ExchangeConvertDataValidator;
import com.app.validators.exchange.impl.ExchangeConvertDataValidatorImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.app.enums.ServletErrorMessages.*;
import static com.app.utils.ServletWriter.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet  {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeServlet.class);

    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final ExchangeResultToDtoMapper exchangeResultToDtoMapper = Mappers.getMapper(ExchangeResultToDtoMapper.class);
    private final ExchangeConvertDataRequestMapper servletRequestMapper = ExchangeConvertDataRequestMapperImpl.getInstance();
    private final ExchangeConvertDataValidator validator = ExchangeConvertDataValidatorImpl.getInstance();


    //Do a exchange
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //Extracts params from request, if field is missing throws RequestMappingEx
            ExchangeConvertData exchangeConvertData = servletRequestMapper.mapFromRequest(req);

            //Checks fields for null or blank
            ValidationResult validationResult = validator.isValid(exchangeConvertData);
            if (validationResult.hasAnyErrors()) {
                writeInvalidResponse(validationResult, resp);
                return;
            }

            ExchangeResult exchangeResult = exchangeRateService.convertCurrency(exchangeConvertData);
            ExchangeDtoResponse exchangeDtoResponse = exchangeResultToDtoMapper.map(exchangeResult);

            mapToJsonAndWriteInResponse(exchangeDtoResponse, resp);
        } catch (RequestMappingException ex) {
            writeErrorResponse(ex, resp, MAPPING_ERROR);
        } catch (ExchangeRateNotFoundException ex) {
            LOG.warn("Exchange rate not found!", ex);
            writeErrorResponse(EX_RATE_NOT_FOUND_ERROR, resp);
        } catch (SQLException ex) {
            LOG.error("Database problem", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }
}
