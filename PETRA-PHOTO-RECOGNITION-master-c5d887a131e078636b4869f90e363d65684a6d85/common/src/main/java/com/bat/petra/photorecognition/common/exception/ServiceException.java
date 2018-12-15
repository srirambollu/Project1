package com.bat.petra.photorecognition.common.exception;

public class ServiceException extends RuntimeException {

    private IRProcessResultCodes resultCode;
    private String resultDetail;

    public ServiceException(IRProcessResultCodes resultCode) {
        this(resultCode, "");
    }

    public ServiceException(IRProcessResultCodes resultCode, String resultDetail) {
        super(resultCode.getResult());
        this.resultCode = resultCode;
        this.resultDetail = resultDetail;
    }

    public IRProcessResultCodes getResultCode() {
        return resultCode;
    }

    public String getResultDetail() {
        return resultDetail;
    }
}
