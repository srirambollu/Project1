package com.bat.petra.photorecognition.common.exception;

public class ValidationException extends Exception {

    private IRProcessResultCodes resultCode;
    private String resultDetail;

    public ValidationException(IRProcessResultCodes resultCode) {
        this(resultCode, "");
    }

    public ValidationException(IRProcessResultCodes resultCode, String resultDetail) {
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
