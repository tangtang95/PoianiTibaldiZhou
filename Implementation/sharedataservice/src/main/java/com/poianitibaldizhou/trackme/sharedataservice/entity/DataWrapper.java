package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class DataWrapper {

    private List<PositionData> positionDataList;
    private List<HealthData> healthDataList;

    public DataWrapper(){
        positionDataList = new ArrayList<>();
        healthDataList = new ArrayList<>();
    }

    public boolean addPositionData(PositionData positionData){
        return positionDataList.add(positionData);
    }

    public boolean addHealthData(HealthData healthData){
        return healthDataList.add(healthData);
    }

}
