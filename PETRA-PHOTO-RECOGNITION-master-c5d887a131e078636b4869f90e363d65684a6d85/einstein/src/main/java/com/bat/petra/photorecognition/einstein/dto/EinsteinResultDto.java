package com.bat.petra.photorecognition.einstein.dto;

import com.bat.petra.photorecognition.einstein.model.Probability;

import java.util.ArrayList;
import java.util.List;

public class EinsteinResultDto {

    private String label;
    private double count;
    private List<Probability> probabilities;

    public EinsteinResultDto(Probability probability) {

        this.label = probability.getLabel();
        this.count = 0;
        this.probabilities = new ArrayList<>();
        addProbability(probability);
    }

    public void addProbability(Probability probability) {

        probabilities.add(probability);
        count++;
    }

    public String getLabel() {
        return label;
    }

    public double getCount() {
        return count;
    }

    public List<Probability> getProbabilities() {
        return probabilities;
    }

    @Override
    public String toString() {
        return "EinsteinResultDto{" +
                "label='" + label + '\'' +
                ", count=" + count +
                ", probabilities=" + probabilities +
                '}';
    }
}