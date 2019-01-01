package com.trackme.trackmeapplication.request.individualRequest.network;

import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.individualRequest.RequestItem;

import java.util.List;

public interface IndividualRequestNetworkIInterface {

    List<RequestItem> getIndividualRequest(String email);

    List<RequestItem> getOwnIndividualRequest(String token) throws ConnectionException;

    void acceptIndividualRequest(String token, String url) throws ConnectionException;

    String refuseIndividualRequest(String token, String url) throws ConnectionException;

    void blockThirdPartyCustomer(String token, String url) throws ConnectionException;

    void send(RequestItem requestItem);
}
