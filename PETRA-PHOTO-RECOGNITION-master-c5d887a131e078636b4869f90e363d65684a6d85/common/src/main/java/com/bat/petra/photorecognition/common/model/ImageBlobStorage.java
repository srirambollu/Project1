package com.bat.petra.photorecognition.common.model;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "image_blob_storage")
public class ImageBlobStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOB_STORAGE_ID_SEQ")
    @SequenceGenerator(name="BLOB_STORAGE_ID_SEQ", sequenceName = "BLOB_STORAGE_ID_SEQ")
    private Long id;

    private String externalId;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProcessStatus status;

    private byte[] data;

    @Column(length = 131072)
    private String result;

    public ImageBlobStorage(String externalId, String fileName, byte[] data) {
        this.externalId = externalId;
        this.fileName = fileName;
        this.data = data;
        this.status = ProcessStatus.NEW;
    }

    public ImageBlobStorage() {
    }

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {

         return "ImageBlobStorage{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", data.length='" + Optional.ofNullable(data).orElse(new byte[0]).length+ '\'' +
                ", status='" + status + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
