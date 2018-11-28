package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.Constants;

public class ThirdPartyNotFoundException extends RuntimeException {

    public ThirdPartyNotFoundException(Long id) {
        super(Constants.thirdPartyNotFoundExceptionMessage + Constants.SPACE + id);
    }
}
