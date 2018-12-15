package com.bat.petra.photorecognition.einstein.client;

import com.bat.petra.photorecognition.einstein.model.TokenResult;
import com.bat.petra.photorecognition.einstein.model.DetectionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class EinsteinClient {

    static Logger LOGGER = LogManager.getLogger(EinsteinClient.class);

    public static final String EINSTEIN_VISION_DETECT_API = "vision/detect";
    public static final String MODEL_ID = "modelId";
    public static final String SAMPLE_CONTENT = "sampleContent";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String EINSTEIN_GET_TOKEN_API = "oauth2/token/";
    public static final String GRANT_TYPE = "grant_type";
    public static final String ASSERTION = "assertion";
    public static final String URN_IETF_PARAMS_OAUTH_GRANT_TYPE_JWT_BEARER = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String VALID_FOR = "valid_for";

    @Autowired
    private RestTemplate restTemplate;

    public DetectionResponse executeDetectRequest(String url, String token, String modelId, File file) {

        HttpEntity<MultiValueMap<String, Object>> request = createDetectRequest(token, modelId, file);

        ResponseEntity<DetectionResponse> response = restTemplate.exchange(
                url + EINSTEIN_VISION_DETECT_API,
                    HttpMethod.POST,
                request,
                DetectionResponse.class
        );

        LOGGER.info(response.getStatusCode() + " " + response.getBody());
        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, Object>> createDetectRequest(String token, String modelId, File file) {

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add(MODEL_ID, modelId);
        bodyMap.add(SAMPLE_CONTENT, new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setCacheControl(CacheControl.noCache());
        headers.set(AUTHORIZATION, BEARER + token);

        return new HttpEntity<>(bodyMap, headers);
    }

    public TokenResult accessTokenRequest(String url, String assertion) {
        return accessTokenRequest(url, assertion, false);
    }

    public TokenResult accessTokenRequest(String url, String assertion, boolean offline) {

        HttpEntity<MultiValueMap<String, Object>> request = createAccessTokenRequest(assertion, offline);

        ResponseEntity<TokenResult> response = restTemplate.exchange(
                url + EINSTEIN_GET_TOKEN_API,
                HttpMethod.POST,
                request,
                TokenResult.class
        );
        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, Object>> createAccessTokenRequest(String assertion, boolean offline) {

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add(GRANT_TYPE, URN_IETF_PARAMS_OAUTH_GRANT_TYPE_JWT_BEARER);
        bodyMap.add(ASSERTION, assertion);

        if (offline) {
            bodyMap.add("scope", "offline");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(bodyMap, headers);
    }

    public TokenResult accessTokenRequest(String url, String refreshToken, String validFor) {

        LOGGER.info("accessTokenRequest refreshToken " + refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = createAccessTokenFromRefreshRequest(refreshToken, validFor);

        ResponseEntity<TokenResult> response = restTemplate.exchange(
                url + EINSTEIN_GET_TOKEN_API,
                    HttpMethod.POST,
                    request,
                    TokenResult.class
        );
        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> createAccessTokenFromRefreshRequest(String refreshToken, String validFor) {

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add(GRANT_TYPE, REFRESH_TOKEN);
        bodyMap.add(REFRESH_TOKEN, refreshToken);
        bodyMap.add(VALID_FOR, validFor);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(bodyMap, headers);
    }
}