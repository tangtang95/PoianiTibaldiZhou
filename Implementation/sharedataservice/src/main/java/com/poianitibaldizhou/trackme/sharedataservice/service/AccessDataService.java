package com.poianitibaldizhou.trackme.sharedataservice.service;

public interface AccessDataService {

    String getIndividualRequestData(Long requestId);
    String getGroupRequestData(Long requestId);
}
