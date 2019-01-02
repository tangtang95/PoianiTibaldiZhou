package com.trackme.trackmeapplication.request.groupRequest.network;

import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequest;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestBuilder;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestWrapper;

import java.util.List;

public interface GroupRequestNetworkInterface {

    List<String> getAggregators();

    List<String> getRequestTypes();

    List<String> getOperators();

    List<String> getDbColumns();

    List<GroupRequestWrapper> getGroupRequest(String token) throws ConnectionException;

    void send(String token, GroupRequestBuilder groupRequestBuilder) throws ConnectionException, RequestNotWellFormedException;
}
