package com.trackme.trackmeapplication.account.network;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.baseUtility.Constant;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * AccountNetworkImp is a class that provide all the function to communicate with the account service
 * on the server.
 */
public class AccountNetworkImp implements AccountNetworkInterface {

    private static AccountNetworkImp instance = null;

    private int accountPort;
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    /**
     * Constructor. (singleton)
     */
    private AccountNetworkImp() {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    /**
     * Getter method.
     *
     * @return a new instance of the class if it is not just created.
     */
    public static AccountNetworkImp getInstance() {
        if(instance == null)
            instance = new AccountNetworkImp();
        return instance;
    }


    @Override
    public String userLogin(String username, String password) throws InvalidDataLoginException {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(
                Constant.PUBLIC_TP_API + Constant.LOGIN_USER_API+"?username="+username+"&password="+password),
                HttpMethod.POST, entity, String.class);
        if (response.getBody().isEmpty())
            throw new InvalidDataLoginException();
        return response.getBody();
    }

    @Override
    public String thirdPartyLogin(String email, String password) throws InvalidDataLoginException {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(
                Constant.PUBLIC_TP_API + Constant.LOGIN_USER_API+"?email="+email+"&password="+password),
                HttpMethod.POST, entity, String.class);
        if (response.getBody().isEmpty())
            throw new InvalidDataLoginException();
        return response.getBody();
    }

    @Override
    public void userLogout(String username) {

    }

    @Override
    public void thirdPartyLogout(String email) {

    }

    @Override
    public void userSignUp(String ssn, String username, String password, String firstName, String lastName, String birthDay, String birthCity, String birthNation) throws UserAlreadySignUpException {

    }

    @Override
    public void thirdPartySignUp(String ssn, String email, String password, String firstName, String lastName, String birthDay, String birthCity, String birthNation) throws UserAlreadySignUpException {

    }

    @Override
    public void companySignUp(String companyName, String email, String password, String address, String dunsNumber) {

    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accessing the resource identified by the uri
     */
    private String createURLWithPort(String uri) {
        return "https://localhost:" + accountPort + uri;
    }
}
