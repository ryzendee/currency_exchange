package com.app.servlets.exchange;

import com.app.dto.response.ExchangeRateDtoResponse;
import com.app.entity.ExchangeRateEntity;
import com.app.exception.ExchangeRateNotFoundException;
import com.app.exception.RequestMappingException;
import com.app.mapper.exchange.ExchangeRateEntityToDtoMapper;
import com.app.mapper.exchange.ExchangeRateUpdateRequestMapper;
import com.app.mapper.exchange.impl.ExchangeRateUpdateRequestMapperImpl;
import com.app.models.exchange.ExchangeRateUpdateData;
import com.app.service.exchange.ExchangeRateService;
import com.app.service.exchange.ExchangeRateServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.app.enums.ServletErrorMessages.*;
import static com.app.utils.PathVariableReader.extractBaseCurrency;
import static com.app.utils.PathVariableReader.extractTargetCurrency;
import static com.app.utils.ServletWriter.mapToJsonAndWriteInResponse;
import static com.app.utils.ServletWriter.writeErrorResponse;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    // example -> /USDEUR
    private static final int PATH_LENGTH = 6;
    private static final String METHOD_PATCH = "PATCH";
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateServlet.class);
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final ExchangeRateEntityToDtoMapper exchangeRateEntityToDtoMapper = Mappers.getMapper(ExchangeRateEntityToDtoMapper.class);
    private final ExchangeRateUpdateRequestMapper servletRequestMapper = ExchangeRateUpdateRequestMapperImpl.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals(METHOD_PATCH)) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    //Get by pair of codes from path variable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (isValidPathVariable(pathInfo)) {
                writeErrorResponse(INVALID_PATH_VARIABLE_ERROR, resp);
                return;
            }

            String baseCurrency = extractBaseCurrency(pathInfo);
            String targetCurrency = extractTargetCurrency(pathInfo);
            ExchangeRateEntity exchangeRate = exchangeRateService.getExchangeRateForCurrencyPair(baseCurrency, targetCurrency);
            ExchangeRateDtoResponse dtoResponse = exchangeRateEntityToDtoMapper.map(exchangeRate);

            mapToJsonAndWriteInResponse(dtoResponse, resp);
        } catch (ExchangeRateNotFoundException ex) {
            LOG.warn("Exchange rate not found exception", ex);
            writeErrorResponse(EX_RATE_NOT_FOUND_ERROR, resp);
        } catch (SQLException ex) {
            LOG.error("Problems with database", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }
    //Update rate for exRate
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (isValidPathVariable(req.getPathInfo())) {
                LOG.warn("Invalid path: {}", req.getPathInfo());
                writeErrorResponse(INVALID_PATH_VARIABLE_ERROR, resp);
                return;
            }

            //Extracts params from request, if field is missing throws RequestMappingEx
            ExchangeRateUpdateData updateData = servletRequestMapper.mapFromRequest(req);

            ExchangeRateEntity updatedRate = exchangeRateService.updateExchangeRateCurrencyPair(updateData);
            ExchangeRateDtoResponse dtoResponse = exchangeRateEntityToDtoMapper.map(updatedRate);

            mapToJsonAndWriteInResponse(dtoResponse, resp);
        } catch (RequestMappingException ex) {
            writeErrorResponse(ex, resp, MAPPING_ERROR);
        } catch (ExchangeRateNotFoundException ex) {
            LOG.warn("Exchange rate not found exception", ex);
            writeErrorResponse(EX_RATE_NOT_FOUND_ERROR, resp);
        } catch (SQLException ex) {
            LOG.error("Problems with database", ex);
            writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
        }
    }
    private boolean isValidPathVariable(String pathInfo) {
        return !StringUtils.isBlank(pathInfo) && pathInfo.length() == PATH_LENGTH;
    }
}
