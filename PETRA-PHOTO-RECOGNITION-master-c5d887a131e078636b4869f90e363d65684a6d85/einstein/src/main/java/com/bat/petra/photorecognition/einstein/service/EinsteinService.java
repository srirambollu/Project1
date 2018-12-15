package com.bat.petra.photorecognition.einstein.service;

import com.bat.petra.photorecognition.einstein.model.DetectionResponse;
import java.io.IOException;

public interface EinsteinService {

    DetectionResponse detectFile(String fileName, byte[] dataFile);
    String getOfflineAccessToken() throws IOException;
}