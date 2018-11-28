package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.Constants;

public class SSNNotFoundException extends RuntimeException{
    public SSNNotFoundException(String ssn) {
        super(Constants.SSN_NOT_FOUND_EXCEPTION_MESSAGE + Constants.SPACE + ssn);
    }
}
