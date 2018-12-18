package com.trackme.trackmeapplication.sharedData.network;

import com.trackme.trackmeapplication.home.userHome.HistoryItem;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;

import java.util.List;

public class SharedDataNetworkImp implements SharedDataNetworkInterface{

    private static SharedDataNetworkImp instance = null;

    private SharedDataNetworkImp() {}

    public static SharedDataNetworkImp getInstance() {
        if(instance == null)
            instance = new SharedDataNetworkImp();
        return instance;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public ThirdPartyInterface getThirdParty(String mail) {
        return null;
    }

    @Override
    public String getGroupRequestData(String requestID) {
        return null;
    }

    @Override
    public String getIndividualRequestData(String requestID) {
        return null;
    }

    @Override
    public List<HistoryItem> getUserData(String username, String startDate, String endDate) {
        return null;
    }
}
