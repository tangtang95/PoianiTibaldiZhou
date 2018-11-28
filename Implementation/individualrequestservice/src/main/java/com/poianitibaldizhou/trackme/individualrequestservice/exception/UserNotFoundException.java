package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.Constants;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String ssn) {
        super(Constants.USER_NOT_FOUND + Constants.SPACE + ssn);
    }
}
