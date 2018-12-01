package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

public class GroupRequestNotFoundException extends RuntimeException {

    public GroupRequestNotFoundException(Long requestId) {
        super(ResourceBundle.getBundle("messagebundle.ShareDataMessages").getString("userNotFoundException") + requestId);
    }

}
