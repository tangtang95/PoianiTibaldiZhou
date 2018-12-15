package com.trackme.trackmeapplication.automatedsos.model;

public final class ThresholdInteger {

    public final Integer max;
    public final Integer min;

    public ThresholdInteger(Integer max, Integer min) {
        this.max = max;
        this.min = min;
    }

    public boolean contains(Integer value){
        return value <= max && value >= min;
    }

}
