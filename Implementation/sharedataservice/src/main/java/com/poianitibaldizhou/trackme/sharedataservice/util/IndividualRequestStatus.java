package com.poianitibaldizhou.trackme.sharedataservice.util;

import java.io.Serializable;

/**
 * Status of an individual request
 */
public enum IndividualRequestStatus implements Serializable {
    PENDING,
    REFUSED,
    ACCEPTED,
    ACCEPTED_UNDER_ANALYSIS
}
