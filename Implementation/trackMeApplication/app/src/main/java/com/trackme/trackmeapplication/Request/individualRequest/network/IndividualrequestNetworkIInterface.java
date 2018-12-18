package com.trackme.trackmeapplication.Request.individualRequest.network;

import com.trackme.trackmeapplication.Request.individualRequest.RequestItem;

import java.util.List;

public interface IndividualrequestNetworkIInterface {

    List<RequestItem> getIndividualRequest(String email);
}
