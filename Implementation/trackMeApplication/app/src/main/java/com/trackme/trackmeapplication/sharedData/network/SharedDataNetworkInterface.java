package com.trackme.trackmeapplication.sharedData.network;

import com.trackme.trackmeapplication.sharedData.ThirdParty;
import com.trackme.trackmeapplication.sharedData.User;

public interface SharedDataNetworkInterface {

    User getUser(String username);

    ThirdParty getThirdParty(String mail);

}
