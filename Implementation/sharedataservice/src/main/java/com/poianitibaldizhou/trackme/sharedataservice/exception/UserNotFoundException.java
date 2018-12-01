package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

/**
 * Exception throw when a specific user has not been found
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new user NOT FOUND exception
     *
     * @param ssn identifier of the NOT FOUND request
     */
    public UserNotFoundException(String ssn) {
        super(ResourceBundle.getBundle("messagebundle.ShareDataMessages").getString("userNotFoundException") + ssn);
    }

}
