package com.trackme.trackmeapplication.Request.individualRequest.network;

import com.trackme.trackmeapplication.Request.individualRequest.RequestItem;

import java.util.List;

public class IndividualRequestNetworkImp implements IndividualrequestNetworkIInterface {

    private static IndividualRequestNetworkImp instance = null;

    private IndividualRequestNetworkImp() {}

    public static IndividualRequestNetworkImp getInstance() {
        if(instance == null)
            instance = new IndividualRequestNetworkImp();
        return instance;
    }

    @Override
    public List<RequestItem> getIndividualRequest(String email) {
        return null;
    }
}
