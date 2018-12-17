package com.trackme.trackmeapplication.sharedData.network;

import com.trackme.trackmeapplication.sharedData.ThirdParty;
import com.trackme.trackmeapplication.sharedData.User;

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
    public ThirdParty getThirdParty(String mail) {
        return null;
    }
}
