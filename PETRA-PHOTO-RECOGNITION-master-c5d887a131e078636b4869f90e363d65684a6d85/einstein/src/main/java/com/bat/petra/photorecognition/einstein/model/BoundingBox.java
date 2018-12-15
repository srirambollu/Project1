package com.bat.petra.photorecognition.einstein.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "label", "probability" })
public class BoundingBox {

    @JsonProperty("maxX")
    private int maxX;
    @JsonProperty("maxY")
    private int maxY;
    @JsonProperty("minX")
    private int minX;
    @JsonProperty("minY")
    private int minY;

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    @Override
    public String toString() {

        return "BoundingBox{" +
                "maxX=" + maxX +
                ", maxY=" + maxY +
                ", minX=" + minX +
                ", minY=" + minY +
                '}';
    }
}
