package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper of data to be received from the user
 */
@Data
@JsonIgnoreProperties
public class DataWrapper {

    private List<PositionData> positionDataList;
    private List<HealthData> healthDataList;

    /**
     * Empty constructor: initialize lists
     */
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
