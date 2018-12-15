package com.bat.petra.photorecognition.einstein.service.impl;

import com.bat.petra.photorecognition.common.exception.EinsteinServiceException;
import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.exception.InternalServerException;
import com.bat.petra.photorecognition.common.model.*;
import com.bat.petra.photorecognition.common.repository.ImageRecognitionProcessRepository;
import com.bat.petra.photorecognition.common.repository.ImageRecognitionProductResultRepository;
import com.bat.petra.photorecognition.common.repository.ImageRecognitionResultDetailRepository;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import com.bat.petra.photorecognition.common.service.ImageStorageService;
import com.bat.petra.photorecognition.common.utils.FileUtils;
import com.bat.petra.photorecognition.einstein.client.EinsteinClient;
import com.bat.petra.photorecognition.einstein.dto.EinsteinResultDto;
import com.bat.petra.photorecognition.einstein.model.DetectionResponse;
import com.bat.petra.photorecognition.einstein.model.TokenResult;
import com.bat.petra.photorecognition.einstein.service.AssertionGeneratorService;
import com.bat.petra.photorecognition.einstein.service.EinsteinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EinsteinServiceImpl implements EinsteinService {

    static Logger LOGGER = LogManager.getLogger(EinsteinService.class);

    @Value("${einstein.model_id}")
    private String EINSTEIN_VISION_MODEL_ID;

    @Value("${einstein.token.expiration_tinme}")
    private String EINSTEIN_TOKEN_EXPIRATION_TIME_IN_SEC;

    @Autowired
    private EinsteinClient einsteinClient;

    @Autowired
    private AssertionGeneratorService assertionGeneratorService;

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private ImageRecognitionProcessRepository imageRecognitionProcessRepository;

    @Autowired
    private ImageRecognitionProductResultRepository imageRecognitionProductResultRepository;

    @Autowired
    private ImageRecognitionResultDetailRepository imageRecognitionResultDetailRepository;

    @PostConstruct
    public void init() {

        this.EINSTEIN_VISION_MODEL_ID = configService.getEinsteinVisionModelId();
    }

    public DetectionResponse detectFile(String fileName, byte[] dataFile) {

        DetectionResponse result = null;
        File tempFile = null;
        String url = configService.getEinsteinUrl();

        try {

            String accessToken = getAccessToken();
            LOGGER.info("accessToken " + accessToken);
            tempFile = FileUtils.writeFile(fileName, dataFile);
            result = einsteinClient.executeDetectRequest(url, accessToken, EINSTEIN_VISION_MODEL_ID, tempFile);
            return result;
        } catch (IOException e) {
            LOGGER.error("Internal server error", e);
            throw new InternalServerException(IRProcessResultCodes.OTHER_ERROR, e.getMessage());
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }

    private String getAccessToken() {

        String accessTokenExpirationTime = configService.getEinsteinAccessTokenExpiresIn();
        LOGGER.info("accessTokenExpirationTime " + accessTokenExpirationTime);
        Instant expirationTime = Instant.parse(accessTokenExpirationTime);
        LOGGER.info("expirationTime " + expirationTime);

        if (Instant.now().isAfter(expirationTime)) {

            LOGGER.info("Getting new token");
            String newAccessToken = refreshToken();
            LOGGER.info("new refreshToken " + newAccessToken);
            return newAccessToken;
        }

        return configService.getEinsteinAccessToken();
    }

    public String getOfflineAccessToken() {

        LOGGER.info("Getting Einstein refresh token");
        String url = configService.getEinsteinUrl();
        String accountId = configService.getEinsteinAccountId();
        String privateKey = configService.getEinsteinPrivateKey();

        LOGGER.info("privateKey:\n" + privateKey);

        float expirationTimeInSec = Float.parseFloat(EINSTEIN_TOKEN_EXPIRATION_TIME_IN_SEC);

        String assertion = assertionGeneratorService.generateJWTAssertion(
                url,
                accountId,
                privateKey,
                expirationTimeInSec
        );

        LOGGER.info("assertion " + assertion);
        TokenResult result = einsteinClient.accessTokenRequest(url, assertion, true);
        LOGGER.info(result);

        Instant expiresIn = Instant.now().plusSeconds(result.getExpiresIn());
        configService.saveTokens(result.getAccessToken(), result.getRefreshToken(), expiresIn.toString());

        return result.getAccessToken();
    }


    private String refreshToken() {

        String url = configService.getEinsteinUrl();
        String refreshToken = configService.getEinsteinRefreshToken();
        TokenResult result = einsteinClient.accessTokenRequest(url, refreshToken, EINSTEIN_TOKEN_EXPIRATION_TIME_IN_SEC);
        LOGGER.info(result);

        Instant expiresIn = Instant.now().plusSeconds(result.getExpiresIn());
        configService.saveAccessToken(result.getAccessToken(), expiresIn.toString());

        return result.getAccessToken();
    }


    @Scheduled(cron = "${einstein.scheduling.job.cron}")
    public void einsteinScheduler(){

        List<ImageBlobStorage> newBlobs = imageStorageService.getNewBlobs();
//        LOGGER.info("einstein scheduler start working new blobs: " + newBlobs.size());
        newBlobs.forEach(this::processImageBlob);
    }

    private void processImageBlob(ImageBlobStorage imageBlobStorage) {

        ImageRecognitionProcess imageRecognitionProcess = startProcess(imageBlobStorage);
        try {
            DetectionResponse detectionResponse = detectFile(imageBlobStorage.getFileName(), imageBlobStorage.getData());
            if(detectionResponse.toString().length() > 131_071) {
                imageBlobStorage.setResult(detectionResponse.toString().substring(0, 131_166) + "...");
            } else {
                imageBlobStorage.setResult(detectionResponse.toString());
            }
            setIRProcessResultCode(imageRecognitionProcess, detectionResponse);

            calculateAndSaveEinsteinResults(imageRecognitionProcess, detectionResponse);
            imageBlobStorage.setStatus(ProcessStatus.PROCESSED);
        } catch (EinsteinServiceException ex) {
            imageRecognitionProcess.setIRProcessResultCode(ex.getResultCode());
            imageBlobStorage.setStatus(ProcessStatus.FAILED);
            imageBlobStorage.setResult(ex.getMessage());
        } finally {
            completeProcess(imageBlobStorage, imageRecognitionProcess);
        }
    }

    private void setIRProcessResultCode(ImageRecognitionProcess imageRecognitionProcess, DetectionResponse detectionResponse) {

        IRProcessResultCodes code =  detectionResponse.getProbabilities().isEmpty() ?
                IRProcessResultCodes.SUCCESS_NO_ITEM_DETECTED :
                IRProcessResultCodes.SUCCESS;

        imageRecognitionProcess.setIRProcessResultCode(code);
    }

    private ImageRecognitionProcess startProcess(ImageBlobStorage imageBlobStorage) {

        LOGGER.info("=== start processing blob\n" + imageBlobStorage);
        imageBlobStorage.setStatus(ProcessStatus.PROCESSING);
        imageStorageService.save(imageBlobStorage);
        ImageRecognitionProcess bySfId = imageRecognitionProcessRepository.findBySfId(imageBlobStorage.getExternalId());
        return bySfId;
    }

    private void calculateAndSaveEinsteinResults(ImageRecognitionProcess imageRecognitionProcess, DetectionResponse detectionResponse) {

        Map<String, EinsteinResultDto> results = new HashMap<>();

        detectionResponse.getProbabilities().forEach(probability -> {

            if (results.containsKey(probability.getLabel())) {

                EinsteinResultDto resultDTO = results.get(probability.getLabel());
                resultDTO.addProbability(probability);

            } else {
                results.put(probability.getLabel(), new EinsteinResultDto(probability));
            }

        });

        saveProductResult(results, imageRecognitionProcess);
        if(detectionResponse.toString().length() > 131_071) {
            imageRecognitionProcess.setResultDetails(detectionResponse.toString().substring(0, 131_166) + "...");
        } else {
            imageRecognitionProcess.setResultDetails(detectionResponse.toString());
        }
    }

    private void saveProductResult(Map<String, EinsteinResultDto> results, ImageRecognitionProcess imageRecognitionProcess) {

        results.forEach((productLabel, resultDTO) -> {

            String productResultExternalId = UUID.randomUUID().toString();

            ImageRecognitionProductResult productResult = new ImageRecognitionProductResult(
                    imageRecognitionProcess,
                    productLabel,
                    resultDTO.getCount(),
                    productResultExternalId
            );

            imageRecognitionProductResultRepository.save(productResult);
            saveDetailResults(imageRecognitionProcess, resultDTO, productResultExternalId);
        });
    }

    private void saveDetailResults(ImageRecognitionProcess imageRecognitionProcess, EinsteinResultDto resultDTO, String productResultExternalId) {

        resultDTO.getProbabilities().forEach(probability -> {

            ImageRecognitionResultDetail resultDetail = new ImageRecognitionResultDetail(
                    imageRecognitionProcess,
                    probability.toString(),
                    (double)probability.getProbability(),
                    productResultExternalId,
                    probability.getLabel()
            );

            imageRecognitionResultDetailRepository.save(resultDetail);
        });
    }

    private void completeProcess(ImageBlobStorage imageBlobStorage, ImageRecognitionProcess imageRecognitionProcess) {

        imageRecognitionProcessRepository.save(imageRecognitionProcess);
        removeBlob(imageBlobStorage);
        imageStorageService.save(imageBlobStorage);

        LOGGER.info("imageRecognitionProcess\n " + imageRecognitionProcess);
        LOGGER.info("blob\n " + imageBlobStorage);
        LOGGER.info("=== process completed");
    }

    private void removeBlob(ImageBlobStorage imageBlobStorage) {
        imageBlobStorage.setData(null);
    }
}
