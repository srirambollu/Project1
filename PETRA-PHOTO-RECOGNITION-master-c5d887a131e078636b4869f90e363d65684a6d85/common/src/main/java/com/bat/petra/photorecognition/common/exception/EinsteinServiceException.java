package com.bat.petra.photorecognition.common.exception;

public class EinsteinServiceException extends ServiceException {

    public EinsteinServiceException(IRProcessResultCodes resultCode) {
        super(resultCode);
    }

    public EinsteinServiceException(IRProcessResultCodes resultCode, String resultDetail) {
        super(resultCode, resultDetail);
    }
}
