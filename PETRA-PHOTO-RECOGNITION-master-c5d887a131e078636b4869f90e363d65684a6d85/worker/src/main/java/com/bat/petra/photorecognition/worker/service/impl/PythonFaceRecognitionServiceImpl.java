package com.bat.petra.photorecognition.worker.service.impl;

import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.exception.ValidationException;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import com.bat.petra.photorecognition.worker.service.FaceDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
public class PythonFaceRecognitionServiceImpl implements FaceDetectionService {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void validateFaces(ByteArrayOutputStream imageStream) throws ValidationException {

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "image/jpeg");

        String faceDetectionUrl = configurationService.getFaceRecogntionUrl() + "/detect-faces";

        try {
            HttpEntity<byte[]> httpEntity = new HttpEntity<>(imageStream.toByteArray(), httpHeaders);
            ResponseEntity responseEntity = restTemplate.exchange(
                    faceDetectionUrl,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            if (Optional.ofNullable(responseEntity.getBody()).isPresent()) {

                String response = (String) responseEntity.getBody();

                if (Integer.parseInt(response) > 0) {
                    throw new ValidationException(IRProcessResultCodes.VALIDATION_FACE_DETECTED);
                }
            }
        } catch (HttpServerErrorException ex) {
            throw new ValidationException(IRProcessResultCodes.VALIDATION_FACE_NOT_COMPLETED);
        }
    }
}
