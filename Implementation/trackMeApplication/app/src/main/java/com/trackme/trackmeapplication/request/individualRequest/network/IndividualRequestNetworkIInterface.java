package com.trackme.trackmeapplication.request.individualRequest.network;

import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequest;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequestWrapper;

import java.util.List;

public interface IndividualRequestNetworkIInterface {

    List<IndividualRequestWrapper> getIndividualRequest(String token) throws ConnectionException;

    List<IndividualRequestWrapper> getOwnIndividualRequest(String token) throws ConnectionException;

    void acceptIndividualRequest(String token, String url) throws ConnectionException;

    String refuseIndividualRequest(String token, String url) throws ConnectionException;

    void blockThirdPartyCustomer(String token, String url) throws ConnectionException;

    void send(String token, IndividualRequest individualRequest, String userSSN) throws ConnectionException, RequestNotWellFormedException;
}
