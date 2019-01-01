package com.trackme.trackmeapplication.sharedData.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.trackme.trackmeapplication.home.userHome.HistoryItem;
import com.trackme.trackmeapplication.httpConnection.BusinessURLManager;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.UserURLManager;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SharedDataNetworkImp implements SharedDataNetworkInterface, LockInterface {

    private static SharedDataNetworkImp instance = null;

    private HttpHeaders httpHeaders;
    private ObjectMapper mapper;

    private UserURLManager userUrlManager = null;
    private BusinessURLManager businessURLManager = null;

    private final Object lock = new Object();
    private boolean isLock;

    private SharedDataNetworkImp() {
        httpHeaders = new HttpHeaders();
        mapper = new ObjectMapper();
    }

    public static SharedDataNetworkImp getInstance() {
        if(instance == null)
            instance = new SharedDataNetworkImp();
        return instance;
    }

    @Override
    public User getUser(String token) throws UserNotFoundException, ConnectionException {
        synchronized (lock) {
            userUrlManager = UserURLManager.getInstance();
            isLock(true);
            httpHeaders.add("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            try {
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.getUserInfoLink()).setHttpMethod(HttpMethod.GET)
                        .setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        //Log.d("BODY", connectionBuilder.getConnection().getResponse());
                        return mapper.readValue(connectionBuilder.getConnection().getResponse(), User.class);
                    case UNAUTHORIZED: throw new ConnectionException();
                    case NOT_FOUND: throw new UserNotFoundException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new UserNotFoundException();
            }
        }
    }

    @Override
    public ThirdPartyInterface getThirdParty(String token) throws UserNotFoundException, ConnectionException {
        synchronized (lock) {
            businessURLManager = BusinessURLManager.getInstance();
            isLock(true);
            httpHeaders.add("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            try {
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getUserInfoLink()).setHttpMethod(HttpMethod.GET)
                        .setEntity(entity).getConnection().start();

                while (isLock)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        List<String> list;
                        try {
                            list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..privateThirdPartyDetail");
                            return mapper.readValue(list.get(0), PrivateThirdPartyDetail.class);
                        } catch (JsonPathException e) {
                            list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..companyDetail");
                            return mapper.readValue(list.get(0), CompanyDetail.class);
                        }
                    case UNAUTHORIZED: throw new ConnectionException();
                    case NOT_FOUND: throw new UserNotFoundException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new UserNotFoundException();
            }
        }
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
    public List<HistoryItem> getUserData(String token, String startDate, String endDate) throws ConnectionException {
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(
                        userUrlManager.getGetOwnDataLink() + "?from=" + startDate + "&to=" + endDate
                        )
                        .setHttpMethod(HttpMethod.GET)
                        .setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();

                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.OK){
                    List<LinkedHashMap<String, String>> list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$.healthDataList");
                    List<HistoryItem> historyItems = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        historyItems.add(mapper.readValue(
                                mapper.writeValueAsString(list.get(i)),
                                HistoryItem.class));
                    }
                    return historyItems;
                }
                throw new ConnectionException();

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Object getLock() {
        return lock;
    }

    @Override
    public void isLock(boolean b) {
        this.isLock = b;
    }
}
