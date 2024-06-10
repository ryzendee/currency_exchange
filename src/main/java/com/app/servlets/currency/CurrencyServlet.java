    package com.app.servlets.currency;

    import com.app.dto.response.CurrencyDtoResponse;
    import com.app.entity.CurrencyEntity;
    import com.app.exception.CurrencyNotFoundException;
    import com.app.mapper.currency.CurrencyDtoMapper;
    import com.app.service.currency.CurrencyService;
    import com.app.service.currency.CurrencyServiceImpl;
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
    import static com.app.utils.ServletWriter.mapToJsonAndWriteInResponse;
    import static com.app.utils.ServletWriter.writeErrorResponse;


    @WebServlet("/currency/*")
    public class CurrencyServlet extends HttpServlet {

        // Example -> /USD
        private static final int PATH_LENGTH = 3;
        private static final Logger LOG = LoggerFactory.getLogger(CurrencyServlet.class);
        private final CurrencyService currencyService = CurrencyServiceImpl.getInstance();
        private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);


        //get by currency code
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            try {
                String pathInfo = req.getPathInfo();

                if (!isPathValid(pathInfo)) {
                    LOG.warn("Invalid path variable: {}", pathInfo);
                    writeErrorResponse(INVALID_PATH_VARIABLE_ERROR, resp);
                    return;
                }

                String currencyCode = extractBaseCurrency(pathInfo);
                CurrencyEntity foundedCurrency = currencyService.getByCurrencyCode(currencyCode);
                CurrencyDtoResponse currencyDto = currencyDtoMapper.map(foundedCurrency);

                mapToJsonAndWriteInResponse(currencyDto, resp);
            } catch (CurrencyNotFoundException ex) {
                LOG.warn("Currency not found exception", ex);
                writeErrorResponse(CURRENCY_NOT_FOUND_ERROR, resp);
            } catch (SQLException ex) {
                LOG.error("Problems with database", ex);
                writeErrorResponse(INTERNAL_SERVER_ERROR, resp);
            }
        }

        private boolean isPathValid(String pathInfo) {
            return !StringUtils.isBlank(pathInfo) && pathInfo.length() == PATH_LENGTH;
        }

    }
