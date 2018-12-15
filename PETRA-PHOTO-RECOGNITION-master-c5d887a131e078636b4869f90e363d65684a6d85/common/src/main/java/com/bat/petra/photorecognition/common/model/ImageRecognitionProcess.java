package com.bat.petra.photorecognition.common.model;

import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ppr_imagerecognitionprocess__c", schema = "salesforce")
public class ImageRecognitionProcess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imagerecognition_generator")
    @SequenceGenerator(name="imagerecognition_generator", sequenceName = "ppr_imagerecognitionprocess__c_id_seq", schema = "salesforce")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 80, updatable=false, insertable=false)
    private String name;

    @Column(name = "fileurl__c", updatable=false, insertable=false)
    private String fileUrl;

    @Column(name = "filename__c", updatable=false, insertable=false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status__c")
    private ProcessStatus status;

    @Column(name = "result__c")
    private String result;

    @Column(name = "resultdetails__c", length = 131072)
    private String resultDetails;

    @Column(name = "type__c", updatable=false, insertable=false)
    private String type;

    @NaturalId
    @Column(name = "sfid", updatable=false, insertable=false)
    private String sfId;

//    @Column(name = "createddate", columnDefinition= "TIMESTAMP WITH TIME ZONE")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;

    public ImageRecognitionProcess() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public String getType() {
        return type;
    }

    public String getSfId() {
        return sfId;
    }

    public void setSfId(String sfId) {
        this.sfId = sfId;
    }

    public void setIRProcessResultCode(IRProcessResultCodes code) {
        this.setStatus(code.getStatus());
        this.setResult(code.getCode());
        this.setResultDetails(code.getResult());
    }

    @Override
    public String toString() {

        return "ImageRecognitionProcess{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                ", status=" + status +
                ", result='" + result + '\'' +
                ", resultDetails='" + resultDetails + '\'' +
                ", sfId='" + sfId + '\'' +
                ", type=" + type +
                '}';
    }
}
