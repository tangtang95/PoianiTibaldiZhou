package com.trackme.trackmeapplication.Request.groupRequest.network;

import com.trackme.trackmeapplication.Request.groupRequest.GroupRequestItem;

import java.util.List;

public interface GroupRequestNetworkInterface {

    List<String> getAggregators();

    List<String> getRequestTypes();

    List<String> getOperators();

    List<String> getDbColumns();

    List<GroupRequestItem> getGroupRequest(String email);

    void send(GroupRequestItem groupRequestItem);
}