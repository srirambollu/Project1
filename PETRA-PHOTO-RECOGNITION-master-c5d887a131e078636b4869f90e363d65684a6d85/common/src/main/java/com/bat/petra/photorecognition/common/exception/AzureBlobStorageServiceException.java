package com.bat.petra.photorecognition.common.exception;

public class AzureBlobStorageServiceException extends ServiceException {

    public AzureBlobStorageServiceException(IRProcessResultCodes resultCode) {
        super(resultCode);
    }

    public AzureBlobStorageServiceException(IRProcessResultCodes resultCode, String resultDetail) {
         super(resultCode, resultDetail);
    }
}
