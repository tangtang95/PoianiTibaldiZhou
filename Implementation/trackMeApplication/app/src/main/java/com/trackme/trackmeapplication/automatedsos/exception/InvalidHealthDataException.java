package com.trackme.trackmeapplication.automatedsos.exception;

import com.trackme.trackmeapplication.localdb.entity.HealthData;

public class InvalidHealthDataException extends Exception {

    public InvalidHealthDataException(HealthData healthData) {
        super("invalid health data value" + healthData.toString());
    }
}
