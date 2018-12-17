package com.trackme.trackmeapplication.automatedsos.model;

import com.trackme.trackmeapplication.automatedsos.exception.InvalidHealthDataException;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

public interface HealthDataInspector {

    boolean isGraveCondition(HealthData healthData) throws InvalidHealthDataException;

}
