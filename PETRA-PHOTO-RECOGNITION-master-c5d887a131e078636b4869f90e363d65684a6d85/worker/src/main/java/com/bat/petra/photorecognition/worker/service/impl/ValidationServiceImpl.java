package com.bat.petra.photorecognition.worker.service.impl;

import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.exception.ValidationException;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import com.bat.petra.photorecognition.worker.service.ValidationService;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ValidationServiceImpl implements ValidationService, InitializingBean {

    private int NUMBER_OF_HOURS_BEFORE_EXPIRATION;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void afterPropertiesSet() throws Exception {

        NUMBER_OF_HOURS_BEFORE_EXPIRATION = Integer.parseInt(
                configurationService.getEinsteinTimeDifferenceLimit()
        );
    }

    public void validateSize(ByteArrayOutputStream stream) throws ValidationException {

        if (stream.size() > Integer.parseInt(configurationService.getMaxSizeLimit())) {

            throw new ValidationException(IRProcessResultCodes.VALIDATION_FILE_TOO_BIG);

        } else if ( stream.size() < Integer.parseInt(configurationService.getMinSizeLimit())) {

            throw new ValidationException(IRProcessResultCodes.VALIDATION_FILE_TOO_SMALL);
        }
    }

    public void validateMetadata(ByteArrayOutputStream stream) throws ValidationException {

        try {

            Metadata metadata = ImageMetadataReader.readMetadata(
                    new ByteArrayInputStream(stream.toByteArray())
            );

            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime yesterday = now.minusHours(Long.valueOf(configurationService.getEinsteinTimeDifferenceLimit()));
            LocalDateTime metadataDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (metadataDate.compareTo(yesterday) < 0) {

                throw new ValidationException(
                        IRProcessResultCodes.VALIDATION_TIME,
                        "Should not be older than " + configurationService.getEinsteinTimeDifferenceLimit() + " hours."
                );
            }

        } catch (ImageProcessingException | IOException ex) {

           throw new ValidationException(IRProcessResultCodes.VALIDATION_METADATA, ex.getMessage());

        } catch (NullPointerException ex) {

            throw new ValidationException(IRProcessResultCodes.VALIDATION_NO_EXIF);
        }
    }
}
