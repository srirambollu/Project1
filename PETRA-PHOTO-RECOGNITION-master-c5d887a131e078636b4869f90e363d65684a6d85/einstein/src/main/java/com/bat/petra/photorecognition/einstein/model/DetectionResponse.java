package com.bat.petra.photorecognition.einstein.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "probabilities", "object" })
public class DetectionResponse {

    @JsonProperty("probabilities")
    private List<Probability> probabilities = new ArrayList<>();

    @JsonProperty("object")
    private String object;


    public List<Probability> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Probability> probabilities) {
        this.probabilities = probabilities;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {

        return "DetectionResponse{" +
                "probabilities=" + probabilities +
                ", object='" + object + '\'' +
                '}';
    }
}
