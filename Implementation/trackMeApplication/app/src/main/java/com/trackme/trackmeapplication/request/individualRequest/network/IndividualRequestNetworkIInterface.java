package com.trackme.trackmeapplication.request.individualRequest.network;

import com.trackme.trackmeapplication.request.individualRequest.RequestItem;

import java.util.List;

public interface IndividualRequestNetworkIInterface {

    List<RequestItem> getIndividualRequest(String email);

    List<RequestItem> getOwnIndividualRequest(String username);

    void acceptIndividualRequest(String requestID);

    void refuseIndividualRequest(String requestID);

    void blockThirdPartyCustomer(String email);

    void send(RequestItem requestItem);
}
