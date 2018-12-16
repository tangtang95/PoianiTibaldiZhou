package com.trackme.trackmeapplication.automatedsos.model;

import com.trackme.trackmeapplication.automatedsos.exception.InvalidHealthDataException;

public interface HealthDataInspector {

    boolean isGraveCondition(HealthData healthData) throws InvalidHealthDataException;

}
