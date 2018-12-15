package com.bat.petra.photorecognition.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "azureintegration__c", schema = "salesforce")
public class AzureIntegration implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "tableendpoint__c")
    private String tableEndpoint;

    @Column(name = "container__c")
    private String container;

    @Column(name = "blobendpoint__c")
    private String blobEndpoint;

    @Column(name = "fileendpoint__c")
    private String fileEndpoint;

    @Column(name = "sharedaccesssignature__c")
    private String sas;

    @Column(name = "queueendpoint__c")
    private String queueEndpoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableEndpoint() {
        return tableEndpoint;
    }

    public void setTableEndpoint(String tableEndpoint) {
        this.tableEndpoint = tableEndpoint;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getBlobEndpoint() {
        return blobEndpoint;
    }

    public void setBlobEndpoint(String blobEndpoint) {
        this.blobEndpoint = blobEndpoint;
    }

    public String getFileEndpoint() {
        return fileEndpoint;
    }

    public void setFileEndpoint(String fileEndpoint) {
        this.fileEndpoint = fileEndpoint;
    }

    public String getSas() {
        return sas;
    }

    public void setSas(String sas) {
        this.sas = sas;
    }

    public String getQueueEndpoint() {
        return queueEndpoint;
    }

    public void setQueueEndpoint(String queueEndpoint) {
        this.queueEndpoint = queueEndpoint;
    }
}
