package com.trackme.trackmeapplication.sharedData.network;

import com.trackme.trackmeapplication.home.userHome.HistoryItem;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.util.List;

public interface SharedDataNetworkInterface {

    User getUser(String token) throws UserNotFoundException, ConnectionException;

    ThirdPartyInterface getThirdParty(String token) throws UserNotFoundException, ConnectionException;

    String getGroupRequestData(String requestID);

    String getIndividualRequestData(String requestID);

    List<HistoryItem> getUserData(String token, String startDate, String endDate) throws ConnectionException;

}
