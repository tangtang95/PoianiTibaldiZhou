package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

/**
 * Exception throw when a specific group request has not been found
 */
public class GroupRequestNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new group request NOT FOUND exception
     *
     * @param requestId identifier of the NOT FOUND request
     */
    public GroupRequestNotFoundException(Long requestId) {
        super(ResourceBundle.getBundle("messagebundle.ShareDataMessages").getString("userNotFoundException") + requestId);
    }

}
