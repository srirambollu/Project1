package com.bat.petra.photorecognition.common.exception;

public class InternalServerException extends ServiceException {

    public InternalServerException(IRProcessResultCodes resultCode) {
        super(resultCode);
    }

    public InternalServerException(IRProcessResultCodes resultCode, String resultDetail) {
        super(resultCode, resultDetail);
    }
}
