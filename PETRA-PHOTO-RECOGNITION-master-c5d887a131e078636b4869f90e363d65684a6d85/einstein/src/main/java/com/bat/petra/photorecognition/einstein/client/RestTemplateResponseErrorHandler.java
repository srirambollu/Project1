package com.bat.petra.photorecognition.einstein.client;

import com.bat.petra.photorecognition.common.exception.EinsteinServiceException;
import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    static Logger LOGGER = LogManager.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (
                httpResponse.getStatusCode().series() ==  HttpStatus.Series.CLIENT_ERROR ||
                        httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
        );
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {

            LOGGER.error("Einstein server error");
            throw new EinsteinServiceException(IRProcessResultCodes.EINSTEIN_ERROR, "Einstein server error");

        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {

            // handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {

                LOGGER.error("Einstein vision not found", httpResponse.getStatusText());
                throw new EinsteinServiceException(IRProcessResultCodes.EINSTEIN_NOT_FOUND, "Einstein service not found");
            }
            if (httpResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                LOGGER.error("401 error try again");
            }

            LOGGER.error("Einstein client error");
            throw new EinsteinServiceException(IRProcessResultCodes.EINSTEIN_ERROR, "Einstein client error");
        }
    }
}