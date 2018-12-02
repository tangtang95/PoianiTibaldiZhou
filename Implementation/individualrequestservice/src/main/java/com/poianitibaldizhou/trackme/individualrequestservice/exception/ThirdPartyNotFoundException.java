package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.Constants;

/**
 * Exception thrown when a third party has not been found
 */
public class ThirdPartyNotFoundException extends RuntimeException {

    /**
     * Creates a new third party not found exception
     *
     * @param id identified of the third party customer not found
     */
    public ThirdPartyNotFoundException(Long id) {
        super(Constants.thirdPartyNotFoundExceptionMessage + Constants.SPACE + id);
    }
}
