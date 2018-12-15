package com.bat.petra.photorecognition.worker.service;

import com.bat.petra.photorecognition.common.exception.ValidationException;

import java.io.ByteArrayOutputStream;
import java.util.Set;

public interface ValidationService {

    void validateSize(ByteArrayOutputStream stream) throws ValidationException;
    void validateMetadata(ByteArrayOutputStream stream) throws ValidationException;
}
