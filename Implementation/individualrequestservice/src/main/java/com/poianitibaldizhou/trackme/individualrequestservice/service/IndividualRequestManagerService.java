package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;

import java.util.List;

public interface IndividualRequestManagerService {
    List<IndividualRequest> getThirdPartyRequests(Long thirdPartyID);
    IndividualRequest addRequest(IndividualRequest newRequest);
    IndividualRequest getRequestById(Long id);
}
