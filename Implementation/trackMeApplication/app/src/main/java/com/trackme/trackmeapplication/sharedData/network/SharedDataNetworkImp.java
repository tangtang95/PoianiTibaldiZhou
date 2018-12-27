package com.trackme.trackmeapplication.sharedData.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.home.userHome.HistoryItem;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SharedDataNetworkImp implements SharedDataNetworkInterface{

    private static SharedDataNetworkImp instance = null;

    private int accountPort;
    private String IPAddress;

    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private ObjectMapper mapper;

    private SharedDataNetworkImp() {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        mapper = new ObjectMapper();
        accountPort = Settings.getServerPort();
        IPAddress = Settings.getServerAddress();
    }

    public static SharedDataNetworkImp getInstance() {
        if(instance == null)
            instance = new SharedDataNetworkImp();
        return instance;
    }

    @Override
    public User getUser(String token) throws UserNotFoundException {
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        //ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(
        //        createURLWithPort(Constant.SECURED_USER_API + Constant.GET_USER_INFO_API),
         //       HttpMethod.GET, entity);
        //connectionAsyncTask.execute();
        //if (connectionAsyncTask.getStatusReturned() != HttpStatus.OK)
        //    throw new UserNotFoundException();
        //try {
        //    return mapper.readValue(connectionAsyncTask.getResponse(), User.class);
        //} catch (IOException e) {
        //    throw new UserNotFoundException();
        //}
        return null;
    }

    @Override
    public ThirdPartyInterface getThirdParty(String token) throws UserNotFoundException {
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(Constant.SECURED_TP_API + Constant.GET_TP_INFO_API),
                HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK)
            throw new UserNotFoundException();
        try {
            return mapper.readValue(response.getBody(), PrivateThirdPartyDetail.class);
        } catch (IOException e) {
            try {
                return mapper.readValue(response.getBody(), CompanyDetail.class);
            } catch (IOException e1) {
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
    public List<HistoryItem> getUserData(String username, String startDate, String endDate) {
        return new ArrayList<>();
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accessing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "https://"+ IPAddress + ":" + accountPort + uri;
    }
}
