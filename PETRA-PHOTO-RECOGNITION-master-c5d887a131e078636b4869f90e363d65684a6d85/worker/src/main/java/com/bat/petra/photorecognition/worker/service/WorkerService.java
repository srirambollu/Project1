package com.bat.petra.photorecognition.worker.service;

import com.bat.petra.photorecognition.common.exception.AzureBlobStorageServiceException;
import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.exception.ValidationException;
import com.bat.petra.photorecognition.common.model.ImageRecognitionProcess;
import com.bat.petra.photorecognition.common.model.ProcessStatus;
import com.bat.petra.photorecognition.common.repository.ImageRecognitionProcessRepository;
import com.bat.petra.photorecognition.common.service.ImageStorageService;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Component
public class WorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerService.class);

    @Autowired
    private AzureService azureClient;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FaceDetectionService faceDetectionService;

    @Autowired
    private ImageRecognitionProcessRepository imageRecognitionProcessRepository;

    @Autowired
    private ValidationService validationService;

    @Scheduled(cron = "${worker.scheduling.job.cron}")
    public void worker() throws InterruptedException, ParseException {

        List<ImageRecognitionProcess> allNew = imageRecognitionProcessRepository.findAllNew();
//        LOGGER.info("worker scheduler start working, image to process: " + allNew.size());

        allNew.forEach(this::processImage);
    }

    private void processImage(ImageRecognitionProcess imageRecognitionProcess) {

        startProcessing(imageRecognitionProcess);

        try {

            validateImageProcessUrl(imageRecognitionProcess);
            ByteArrayOutputStream outputStream = downloadBlob(imageRecognitionProcess);
            validateImage(outputStream);
            imageStorageService.save(imageRecognitionProcess, outputStream.toByteArray());

        } catch (ValidationException validationException) {

            LOGGER.info("Validation exception: " + validationException.getResultDetail());
            mapIRProcessResultCode(
                    imageRecognitionProcess,
                    validationException.getResultCode(),
                    validationException.getResultDetail()
            );

        } finally {

            imageRecognitionProcessRepository.save(imageRecognitionProcess);
        }
    }

    private void startProcessing(ImageRecognitionProcess imageRecognitionProcess) {

        LOGGER.info("starting process " + imageRecognitionProcess);
        imageRecognitionProcess.setStatus(ProcessStatus.PROCESSING);
        imageRecognitionProcessRepository.save(imageRecognitionProcess);
    }

    private void validateImage(ByteArrayOutputStream outputStream) throws ValidationException {

        validationService.validateSize(outputStream);

        if(Boolean.valueOf(configurationService.getTimeValidationEnabledFlag())) {
            validationService.validateMetadata(outputStream);
        }

        if(Boolean.valueOf(configurationService.getFaceValidationEnabledFlag())) {
            faceDetectionService.validateFaces(outputStream);
        }
    }

    private ByteArrayOutputStream downloadBlob(ImageRecognitionProcess imageRecognitionProcess) throws ValidationException {

        try {

            String filename = imageRecognitionProcess.getFileName();

            if(!filename.endsWith("jpg") && !filename.endsWith(".jpeg")) {

                throw new ValidationException(IRProcessResultCodes.VALIDATION_FILE_EXTENSION);
            }

            return azureClient.downloadBlob(imageRecognitionProcess.getFileUrl());

        } catch (AzureBlobStorageServiceException ex) {

            throw new ValidationException(IRProcessResultCodes.AZURE_DOWNLOAD_ERROR, ex.getMessage());
        }
    }

    private void validateImageProcessUrl(ImageRecognitionProcess imageRecognitionProcess) throws ValidationException {

        if ( !Optional.ofNullable(imageRecognitionProcess.getFileName()).isPresent() || imageRecognitionProcess.getFileName().isEmpty()) {

            throw new ValidationException(IRProcessResultCodes.VALIDATION_FILE_NAME);
        }
        if ( !Optional.ofNullable(imageRecognitionProcess.getFileUrl()).isPresent() || imageRecognitionProcess.getFileUrl().isEmpty()) {

            throw new ValidationException(IRProcessResultCodes.VALIDATION_URL);
        }
    }

    private void mapIRProcessResultCode(ImageRecognitionProcess imageRecognitionProcess, IRProcessResultCodes code, String resultDetail) {

        imageRecognitionProcess.setStatus(code.getStatus());
        imageRecognitionProcess.setResult(code.getCode());
        imageRecognitionProcess.setResultDetails(code.getResult() + " " + resultDetail);
    }
}