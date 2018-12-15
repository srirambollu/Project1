package com.bat.petra.photorecognition.einstein.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "label", "probability" })
public class Probability {

    @JsonProperty("label")
    private String label;
    @JsonProperty("probability")
    private float probability;
    @JsonProperty("boundingBox")
    private BoundingBox boundingBox;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;

    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public String toString() {

        return "Probability{" +
                "label='" + label + '\'' +
                ", probability=" + probability +
                ", boundingBox=" + boundingBox +
                '}';
    }
}
