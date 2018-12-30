package com.trackme.trackmeapplication.account.network;

import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadyLogoutException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.BusinessURLManager;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.UserURLManager;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyCompanyWrapper;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.ThirdPartyPrivateWrapper;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * AccountNetworkImp is a class that provide all the function to communicate with the account service
 * on the server.
 */
public class AccountNetworkImp implements AccountNetworkInterface, LockInterface {

    private static AccountNetworkImp instance = null;

    private HttpHeaders httpHeaders;

    private String token;
    private UserURLManager userUrlManager = null;
    private BusinessURLManager businessURLManager = null;


    private final Object lock = new Object();
    private boolean isLock;


    /**
     * Constructor. (singleton)
     */
    private AccountNetworkImp() {
        httpHeaders = new HttpHeaders();
    }

    public static AccountNetworkImp getInstance(){
        if(instance == null)
            instance = new AccountNetworkImp();
        return instance;
    }


    @Override
    public String userLogin(String username, String password) throws InvalidDataLoginException, ConnectionException {
        synchronized (lock) {
            userUrlManager = UserURLManager.getInstance();
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            isLock(true);
            try {

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                String url = userUrlManager.createURLWithPort(
                        Constant.PUBLIC_USER_API + Constant.LOGIN_USER_API + "?username=" + username + "&password=" + password);
                connectionBuilder.setUrl(url).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (isLock)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        //Log.d("BODY", connectionBuilder.getConnection().getResponse());
                        userUrlManager.setUrls(JsonPath.read(connectionBuilder.getConnection().getResponse(), "$.._links"));
                        List<String> list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..token");
                        token = list.get(0);
                        return token;
                    case UNAUTHORIZED: throw new InvalidDataLoginException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public String thirdPartyLogin(String email, String password) throws InvalidDataLoginException, ConnectionException {
        synchronized (lock) {
            businessURLManager = BusinessURLManager.getInstance();
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            isLock(true);
            try {

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                String url = businessURLManager.createURLWithPort(
                        Constant.PUBLIC_TP_API + Constant.LOGIN_USER_API+"?email="+email+"&password="+password);
                connectionBuilder.setUrl(url).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (isLock)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        //Log.d("BODY", connectionBuilder.getConnection().getResponse());
                        businessURLManager.setUrls(JsonPath.read(connectionBuilder.getConnection().getResponse(), "$.._links"));
                        List<String> list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..token");
                        token = list.get(0);
                        return token;
                    case UNAUTHORIZED: throw new InvalidDataLoginException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void userLogout() throws UserAlreadyLogoutException {
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.getLogoutLink()).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (isLock)
                    lock.wait();

                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.OK)
                    throw new UserAlreadyLogoutException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void thirdPartyLogout() throws UserAlreadyLogoutException {
        synchronized (lock) {
            isLock(true);
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getLogoutLink()).setHttpMethod(HttpMethod.POST)
                        .setEntity(entity).getConnection().start();

                while (isLock)
                    lock.wait();

                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.OK)
                    throw new UserAlreadyLogoutException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void userSignUp(User user) throws UserAlreadySignUpException {
        synchronized (lock) {
            userUrlManager = UserURLManager.getInstance();
            isLock(true);
            try {
                HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.createURLWithPort(Constant.PUBLIC_USER_API + Constant.REGISTER_USER_API ))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (isLock)
                    lock.wait();

                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.CREATED)
                    throw new UserAlreadySignUpException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void thirdPartySignUp(PrivateThirdPartyDetail privateThirdPartyDetail) throws UserAlreadySignUpException {
        synchronized (lock) {
            businessURLManager = BusinessURLManager.getInstance();
            isLock(true);
            try {
                ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = new ThirdPartyPrivateWrapper();
                thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);

                HttpEntity<ThirdPartyPrivateWrapper> entity = new HttpEntity<>(thirdPartyPrivateWrapper, httpHeaders);
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.createURLWithPort(Constant.PUBLIC_TP_API + Constant.REGISTER_PRIVATE_TP_API))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.CREATED)
                    throw new UserAlreadySignUpException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void companySignUp(CompanyDetail companyDetail) throws UserAlreadySignUpException {
        synchronized (lock) {
            businessURLManager = BusinessURLManager.getInstance();
            isLock(true);
            try {
                ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
                thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);

                HttpEntity<ThirdPartyCompanyWrapper> entity = new HttpEntity<>(thirdPartyCompanyWrapper, httpHeaders);
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.createURLWithPort(Constant.PUBLIC_TP_API + Constant.REGISTER_COMPANY_TP_API))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (isLock)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.CREATED)
                    throw new UserAlreadySignUpException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUser() throws UserNotFoundException, ConnectionException {
        SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
        return sharedDataNetwork.getUser(token);
    }

    @Override
    public ThirdPartyInterface getThirdParty() throws UserNotFoundException, ConnectionException {
        SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
        return sharedDataNetwork.getThirdParty(token);
    }



    @Override
    public  Object getLock() {
        return lock;
    }

    @Override
    public void isLock(boolean b) {
        this.isLock = b;
    }


}
