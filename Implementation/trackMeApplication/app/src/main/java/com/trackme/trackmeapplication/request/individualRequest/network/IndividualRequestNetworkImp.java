package com.trackme.trackmeapplication.request.individualRequest.network;

import com.trackme.trackmeapplication.request.individualRequest.RequestItem;

import java.util.List;

public class IndividualRequestNetworkImp implements IndividualRequestNetworkIInterface {

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

    @Override
    public List<RequestItem> getOwnIndividualRequest(String username) {
        return null;
    }

    @Override
    public void acceptIndividualRequest(String requestID) {

    }

    @Override
    public void refuseIndividualRequest(String requestID) {

    }

    @Override
    public void blockThirdPartyCustomer(String email) {

    }

    @Override
    public void send(RequestItem requestItem) {

    }
}
