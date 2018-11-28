package com.poianitibaldizhou.trackme.sharedataservice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String ssn) {
        super("Could not find user " + ssn);
    }

}
