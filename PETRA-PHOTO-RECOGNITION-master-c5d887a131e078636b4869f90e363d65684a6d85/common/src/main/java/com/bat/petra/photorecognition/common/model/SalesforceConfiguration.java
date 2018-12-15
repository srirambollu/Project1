package com.bat.petra.photorecognition.common.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "configuration__c", schema = "salesforce")
public class SalesforceConfiguration {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "configurationcode__c")
    private String code;

    @Column(name = "description__c")
    private String description;

    @Column(name = "value__c", length = 4096)
    private String value;

    @Column(name = "recordtypename__c")
    private String recordTypeName;

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

    @Column(name = "isactive__c")
    private boolean isActive;

    public SalesforceConfiguration() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) { this.value = value; }

    public String getTableEndpoint() {
        return tableEndpoint;
    }

    public String getContainer() {
        return container;
    }

    public String getBlobEndpoint() {
        return blobEndpoint;
    }

    public String getFileEndpoint() {
        return fileEndpoint;
    }

    public String getSas() {
        return sas;
    }

    public String getQueueEndpoint() {
        return queueEndpoint;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "SalesforceConfiguration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", recordTypeName='" + recordTypeName + '\'' +
                ", tableEndpoint='" + tableEndpoint + '\'' +
                ", container='" + container + '\'' +
                ", blobEndpoint='" + blobEndpoint + '\'' +
                ", fileEndpoint='" + fileEndpoint + '\'' +
                ", sas='" + sas + '\'' +
                ", queueEndpoint='" + queueEndpoint + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}