package com.trackme.trackmeapplication.request.individualRequest.network;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.UserURLManager;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.individualRequest.RequestItem;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class IndividualRequestNetworkImp implements IndividualRequestNetworkIInterface, LockInterface {

    private static IndividualRequestNetworkImp instance = null;

    private HttpHeaders httpHeaders;
    private ObjectMapper mapper;

    private final Object lock = new Object();
    private boolean isLock;

    private IndividualRequestNetworkImp() {
        httpHeaders = new HttpHeaders();
        mapper = new ObjectMapper();
    }

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
    public List<RequestItem> getOwnIndividualRequest(String token) throws ConnectionException {
        synchronized (lock) {
            isLock(true);
            UserURLManager userUrlManager = UserURLManager.getInstance();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.getPendingRequestsLink())
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.OK){
                    List<List<LinkedHashMap<String, String>>> list = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..individualRequestWrapperList");
                    List<String> links = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..href");
                    List<RequestItem> requestItems = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        RequestItem requestItem = mapper.readValue(
                                mapper.writeValueAsString(list.get(0).get(i)),
                                RequestItem.class);
                        requestItem.setResponseLink(links.get(i));
                        requestItems.add(requestItem);
                    }
                    return requestItems;
                }
                throw new ConnectionException();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public void acceptIndividualRequest(String token, String url) throws ConnectionException {
        String responseUrl = getResponseLink(url);
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>("ACCEPT", httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(responseUrl)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.OK)
                    throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String refuseIndividualRequest(String token,String url) throws ConnectionException {
        String responseUrl = getResponseLink(url);
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>("REFUSE", httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(responseUrl)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.OK) {
                    List<String> links = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..blockThirdParty.href");
                    return links.get(0);
                } else throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void blockThirdPartyCustomer(String token, String url) throws ConnectionException {
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(url)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.OK)
                    throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(RequestItem requestItem) {
        /*TODO*/
    }

    private String getResponseLink(String url) throws ConnectionException {
        synchronized (lock) {
            isLock(true);
            try {
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(url)
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.OK){
                    List<String> links = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..href");
                    return links.get(0);
                }
                throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
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
