package com.bat.petra.photorecognition.common.service;

import com.bat.petra.photorecognition.common.model.ImageBlobStorage;
import com.bat.petra.photorecognition.common.model.ImageRecognitionProcess;
import com.bat.petra.photorecognition.common.model.ProcessStatus;
import com.bat.petra.photorecognition.common.repository.ImageBlobStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageStorageService {

    @Autowired
    private ImageBlobStorageRepository imageBlobStorageRepository;

    public ImageBlobStorage save(ImageRecognitionProcess imageRecognitionProcess, byte[] data) {

        return this.save(
                new ImageBlobStorage(
                        imageRecognitionProcess.getSfId(),
                        imageRecognitionProcess.getFileName(),
                        data
                )
        );
    }

    public ImageBlobStorage save(ImageBlobStorage blob) {
        return imageBlobStorageRepository.save(blob);
    }

    public List<ImageBlobStorage> getNewBlobs() {
        return imageBlobStorageRepository.findByStatus(ProcessStatus.NEW);
    }
 }