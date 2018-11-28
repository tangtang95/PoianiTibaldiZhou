package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.Constants;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(Long id) {
        super(Constants.REQUEST_ID_NOT_FOUND + Constants.SPACE + id);
    }
}
