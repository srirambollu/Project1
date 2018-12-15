package com.bat.petra.photorecognition.worker.service;

import com.bat.petra.photorecognition.common.exception.ValidationException;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.Set;

public interface FaceDetectionService {

    void validateFaces(ByteArrayOutputStream imageStream) throws ValidationException;
}
