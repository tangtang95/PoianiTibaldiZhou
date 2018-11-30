package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String ssn) {
        super(ResourceBundle.getBundle("messagebundle.ShareDataMessages").getString("userNotFoundException") + ssn);
    }

}
