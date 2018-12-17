package com.trackme.trackmeapplication.account.network;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;

/**
 * Network interface with the methods to communicate with the Account service on the server side.
 *
 * @author Mattia Tibaldi
 */
public interface AccountNetworkInterface {

    /**
     * User login on the server.
     */
    void userLogin() throws InvalidDataLoginException;

    /**
     * ThirdParty login on the server
     */
    void thirdPartyLogin() throws InvalidDataLoginException;

    /**
     * User logout from the server.
     */
    void userLogout();

    /**
     * ThirdParty logout from the server.
     */
    void thirdPartyLogout();

    /**
     * User sign up on server
     */
    void userSignUp();

    /**
     * Third party sign up on server.
     */
    void thirdPartySignUp();

}
