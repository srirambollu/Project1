package com.bat.petra.photorecognition.common.exception;

import com.bat.petra.photorecognition.common.model.ProcessStatus;

/**
 * Image recognition process result codes list.
 */
public enum IRProcessResultCodes {

    SUCCESS(ProcessStatus.PROCESSED, "000", "Process finished properly."),
    SUCCESS_NO_ITEM_DETECTED(ProcessStatus.PROCESSED, "001", "No item detected on image."),
    VALIDATION_FILE_TOO_SMALL(ProcessStatus.FAILED, "301", "File too small."),
    VALIDATION_FILE_TOO_BIG(ProcessStatus.FAILED, "302", "File too big."),
    VALIDATION_TIME(ProcessStatus.FAILED, "305", "Picture too old."),
    VALIDATION_FACE_DETECTED(ProcessStatus.FAILED, "307", "Face detected."),
    VALIDATION_NO_EXIF(ProcessStatus.FAILED, "308", "No exif metadata in picture."),
    VALIDATION_METADATA(ProcessStatus.FAILED, "309", "Validation metadata error."),
    VALIDATION_URL(ProcessStatus.FAILED, "310", "No file URL."),
    VALIDATION_FILE_EXTENSION(ProcessStatus.FAILED, "311", "Unsupported file format"),
    VALIDATION_FILE_NAME(ProcessStatus.FAILED, "312", "No file name"),
    VALIDATION_FACE_NOT_COMPLETED(ProcessStatus.FAILED, "313", "Face validation not completed"),
    AZURE_AUTHENTICATION_ERROR(ProcessStatus.FAILED, "501", "Problem with authentication occurred."),
    AZURE_DOWNLOAD_ERROR(ProcessStatus.FAILED, "502", "Download failed from Azure Blob Storage."),
    EINSTEIN_ERROR(ProcessStatus.FAILED, "601", "Einstein vision error."),
    EINSTEIN_NOT_FOUND(ProcessStatus.FAILED, "602", "Einstein vision service not found."),
    OTHER_ERROR(ProcessStatus.FAILED, "701", "Unknown error.");

    private ProcessStatus status;
    private String code;
    private String result;

    IRProcessResultCodes(ProcessStatus status, String code, String result) {
        this.status = status;
        this.code = code;
        this.result = result;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

}
