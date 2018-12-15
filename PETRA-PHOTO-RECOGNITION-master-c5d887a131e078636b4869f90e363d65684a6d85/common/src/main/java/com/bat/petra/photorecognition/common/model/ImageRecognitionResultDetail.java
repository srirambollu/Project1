package com.bat.petra.photorecognition.common.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

    @Entity
    @Table(name = "ppr_imagerecognitionresultdetail__c", schema = "salesforce")
    public class ImageRecognitionResultDetail implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @SequenceGenerator(name = "imagerecognitionresultdetail", sequenceName = "ppr_imagerecognitionresultdetail__c_id_seq",
                schema = "salesforce", allocationSize = 1, initialValue = 1)
        @Basic(optional = false)
        @Column(name = "id")
        private Integer id;

        @ManyToOne(fetch=FetchType.EAGER)
        @JoinColumn(name="imagerecognitionproductresult__c", referencedColumnName = "sfid", nullable = false, updatable = false)
        private ImageRecognitionProductResult imageRecognitionProductResult;

        @ManyToOne(fetch=FetchType.EAGER)
        @JoinColumn(name="imagerecognitionprocess__c", referencedColumnName = "sfid", nullable = false, updatable = false)
        private ImageRecognitionProcess imageRecognitionProcess;

        @Column(name = "name", length = 80, updatable=false, insertable=false)
        private String name;

        @Column(name = "result__c")
        private String result;

        @Column(name = "probability__c")
        private Double probability;

        @Column(name = "external_id__c")
        private String externalId;

        @Column(name = "imagerecognitionproductresult__r__external_id__c")
        private String iRProductResultExternalId;

        @NaturalId
        @Column(name = "sfid")
        private String sfId;

        @Column(name = "productcode__c")
        private String productCode;

        public ImageRecognitionResultDetail() {
        }

        public ImageRecognitionResultDetail(
                ImageRecognitionProcess imageRecognitionProcess,
                String result,
                Double probability,
                String iRProductResultExternalId,
                String productCode) {
            this.imageRecognitionProcess = imageRecognitionProcess;
            this.result = result;
            this.probability = probability * 100;
            this.iRProductResultExternalId = iRProductResultExternalId;
            this.productCode = productCode;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public ImageRecognitionProductResult getImageRecognitionProductResult() {
            return imageRecognitionProductResult;
        }

        public void setImageRecognitionProductResult(ImageRecognitionProductResult imageRecognitionProductResult) {
            this.imageRecognitionProductResult = imageRecognitionProductResult;
        }

        public ImageRecognitionProcess getImageRecognitionProcess() {
            return imageRecognitionProcess;
        }

        public void setImageRecognitionProcess(ImageRecognitionProcess imageRecognitionProcess) {
            this.imageRecognitionProcess = imageRecognitionProcess;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Double getProbability() {
            return probability;
        }

        public void setProbability(Double probability) {
            this.probability = probability;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public String getiRProductResultExternalId() {
            return iRProductResultExternalId;
        }

        public void setiRProductResultExternalId(String iRProductResultExternalId) {
            this.iRProductResultExternalId = iRProductResultExternalId;
        }

        public String getSfId() {
            return sfId;
        }

        public void setSfId(String sfId) {
            this.sfId = sfId;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        @Override
        public String toString() {
            return "ImageRecognitionResultDetail{" +
                    "id=" + id +
                    ", imageRecognitionProductResult=" + imageRecognitionProductResult +
                    ", imageRecognitionProcess=" + imageRecognitionProcess +
                    ", name='" + name + '\'' +
                    ", result='" + result + '\'' +
                    ", probability=" + probability +
                    ", externalId='" + externalId + '\'' +
                    ", iRProductResultExternalId='" + iRProductResultExternalId + '\'' +
                    ", sfId='" + sfId + '\'' +
                    ", productCode='" + productCode + '\'' +
                    '}';
        }
    }
