package com.trackme.trackmeapplication.request.groupRequest.network;

import com.trackme.trackmeapplication.request.groupRequest.GroupRequestItem;

import java.util.ArrayList;
import java.util.List;

public class GroupRequestNetworkImp implements GroupRequestNetworkInterface {

    private static GroupRequestNetworkImp instance = null;

    private GroupRequestNetworkImp() {}

    public static GroupRequestNetworkImp getInstance() {
        if(instance == null)
            instance = new GroupRequestNetworkImp();
        return instance;
    }

    @Override
    public List<String> getAggregators() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getRequestTypes() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getOperators() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getDbColumns() {
        return new ArrayList<>();
    }

    @Override
    public List<GroupRequestItem> getGroupRequest(String email) {
        return null;
    }

    @Override
    public void send(GroupRequestItem groupRequestItem) {

    }
}
