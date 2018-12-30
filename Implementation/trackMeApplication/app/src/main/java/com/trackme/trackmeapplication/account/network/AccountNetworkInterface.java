package com.trackme.trackmeapplication.account.network;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadyLogoutException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

/**
 * Network interface with the methods to communicate with the Account service on the server side.
 *
 * @author Mattia Tibaldi
 */
public interface AccountNetworkInterface {

    /**
     * User login on the server.
     */
    String userLogin(String username, String password) throws InvalidDataLoginException, ConnectionException;

    /**
     * PrivateThirdPartyDetail login on the server
     */
    String thirdPartyLogin(String email, String password) throws InvalidDataLoginException, ConnectionException;

    /**
     * User logout from the server.
     */
    void userLogout() throws UserAlreadyLogoutException;

    /**
     * PrivateThirdPartyDetail logout from the server.
     */
    void thirdPartyLogout() throws UserAlreadyLogoutException;

    /**
     * User sign up on server
     */
    void userSignUp(User user) throws UserAlreadySignUpException;

    /**
     * Third party sign up on server.
     */
    void thirdPartySignUp(PrivateThirdPartyDetail privateThirdPartyDetail) throws UserAlreadySignUpException;

    /**
     * CompanyDetail sign up on server.
     */
    void companySignUp(CompanyDetail companyDetail) throws UserAlreadySignUpException;

    /**
     * Getter method.
     *
     * @return the user data saved in the server.
     */
    User getUser() throws UserNotFoundException, ConnectionException;

    /**
     * Getter method.
     *
     * @return the third party data saved in the server.
     */
    ThirdPartyInterface getThirdParty() throws UserNotFoundException, ConnectionException;

}
