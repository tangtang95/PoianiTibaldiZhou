package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.poianitibaldizhou.trackme.sharedataservice.entity.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;

public interface SendDataService {

    HealthData sendHealthData(String userId, HealthData healthData);
    PositionData sendPosition(String userId, PositionData positionData);
    DataWrapper sendClusterOfData(String userId, DataWrapper data);
}
