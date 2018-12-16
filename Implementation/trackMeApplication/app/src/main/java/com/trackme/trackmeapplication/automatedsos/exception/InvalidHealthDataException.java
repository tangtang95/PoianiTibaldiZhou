package com.trackme.trackmeapplication.automatedsos.exception;

import com.trackme.trackmeapplication.automatedsos.model.HealthData;

public class InvalidHealthDataException extends Exception {

    public InvalidHealthDataException(HealthData healthData) {
        super("invalid health data value" + healthData.toString());
    }
}
