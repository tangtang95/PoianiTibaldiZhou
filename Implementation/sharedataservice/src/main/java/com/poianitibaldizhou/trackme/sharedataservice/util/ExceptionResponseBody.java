package com.poianitibaldizhou.trackme.sharedataservice.util;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExceptionResponseBody {

    private Timestamp timestamp;

    private int status;

    private String error;

    private String message;

    public ExceptionResponseBody(Timestamp timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
