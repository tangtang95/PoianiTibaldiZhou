package com.poianitibaldizhou.trackme.individualrequestservice.util;

import java.io.Serializable;

public enum IndividualRequestStatus implements Serializable {
    PENDING,
    REFUSED,
    ACCEPTED,
    ACCEPTED_UNDER_ANALYSIS,
    ELAPSED
}
