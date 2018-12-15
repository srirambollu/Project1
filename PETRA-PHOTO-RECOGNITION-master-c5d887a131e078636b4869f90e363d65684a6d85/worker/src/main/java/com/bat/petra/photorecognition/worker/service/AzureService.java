package com.bat.petra.photorecognition.worker.service;

import java.io.ByteArrayOutputStream;

public interface AzureService {

    ByteArrayOutputStream downloadBlob(String filename);
}
