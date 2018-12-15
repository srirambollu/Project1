package com.bat.petra.photorecognition.common.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ppr_imagerecognitionproductresult__c", schema = "salesforce")
public class ImageRecognitionProductResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "imagerecognitionresult_generator", sequenceName = "ppr_imagerecognitionproductresult__c_id_seq",
            schema = "salesforce", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="imagerecognitionprocess__c", referencedColumnName = "sfid", nullable = false, updatable = false)
    private ImageRecognitionProcess imageRecognitionProcess;

    @Column(name = "name", length = 80, updatable=false, insertable=false)
    private String name;

    @Column(name = "productcode__c")
    private String productCode;

    @Column(name = "numberofproducts__c")
    private Double numberOfProducts;

    @Column(name = "external_id__c")
    private String externalId;

    @NaturalId
    @Column(name = "sfid")
    private String sfId;

    public ImageRecognitionProductResult() {
    }

    public ImageRecognitionProductResult(
            ImageRecognitionProcess imageRecognitionProcess, String productCode, Double numberOfProducts, String externalId
    ) {
        this.imageRecognitionProcess = imageRecognitionProcess;
        this.productCode = productCode;
        this.numberOfProducts = numberOfProducts;
        this.externalId = externalId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Double numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getSfId() {
        return sfId;
    }

    public void setSfId(String sfId) {
        this.sfId = sfId;
    }

    @Override
    public String toString() {

        return "ImageRecognitionProductResult{" +
                "id=" + id +
                ", imageRecognitionProcess=" + imageRecognitionProcess +
                ", name='" + name + '\'' +
                ", productCode='" + productCode + '\'' +
                ", numberOfProducts=" + numberOfProducts +
                ", externalId='" + externalId + '\'' +
                ", sfId='" + sfId + '\'' +
                '}';
    }
}
