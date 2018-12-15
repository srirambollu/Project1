package com.bat.petra.photorecognition.worker.service.impl;

import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.exception.ValidationException;
import com.bat.petra.photorecognition.worker.service.FaceDetectionService;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FaceDetectionServiceImpl implements FaceDetectionService {

    private HaarCascadeDetector frontDetector;
    private HaarCascadeDetector profileDetector;

    public FaceDetectionServiceImpl() {

        this.frontDetector = HaarCascadeDetector.BuiltInCascade.frontalface_alt2.load();
        this.profileDetector = HaarCascadeDetector.BuiltInCascade.profileface.load();
        tuneDetectors(1.2f, 20, 500);
    }

    private void tuneDetectors(float scaleFactor, int minSize, int maxSize) {
        this.frontDetector.setScale(scaleFactor);
        this.profileDetector.setScale(scaleFactor);

        this.frontDetector.setMinSize(minSize);
        this.profileDetector.setMinSize(minSize);

        this.frontDetector.setMaxSize(maxSize);
        this.profileDetector.setMaxSize(maxSize);
    }

    public void validateFaces(ByteArrayOutputStream imageStream) throws ValidationException {

        try (InputStream inputStream = new ByteArrayInputStream(imageStream.toByteArray())) {

            MBFImage frame = ImageUtilities.readMBF(inputStream);

            List<DetectedFace> facesList = frontDetector.detectFaces(Transforms.calculateIntensity(frame));
            List<DetectedFace> profiles = profileDetector.detectFaces(Transforms.calculateIntensity(frame));

            facesList.addAll(profiles);

            if(facesList.isEmpty()) {
                throw new ValidationException(IRProcessResultCodes.VALIDATION_FACE_DETECTED);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}