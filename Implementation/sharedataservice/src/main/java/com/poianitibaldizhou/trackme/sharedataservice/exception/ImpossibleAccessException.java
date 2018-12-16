package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.IMPOSSIBLE_ACCESS_EXCEPTION_KEY;
import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.RESOURCE_STRING_PATH;

/**
 * Exception for handling not granted accesses to controller methods
 */
public class ImpossibleAccessException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new impossible access exception
     */
    public ImpossibleAccessException() {
        super(ResourceBundle.getBundle(RESOURCE_STRING_PATH)
                .getString(IMPOSSIBLE_ACCESS_EXCEPTION_KEY));
    }
}
