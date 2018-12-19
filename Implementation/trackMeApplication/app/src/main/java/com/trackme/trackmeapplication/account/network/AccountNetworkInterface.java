package com.trackme.trackmeapplication.account.network;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;

/**
 * Network interface with the methods to communicate with the Account service on the server side.
 *
 * @author Mattia Tibaldi
 */
public interface AccountNetworkInterface {

    /**
     * User login on the server.
     */
    String userLogin(String username, String password) throws InvalidDataLoginException;

    /**
     * ThirdParty login on the server
     */
    String thirdPartyLogin(String email, String password) throws InvalidDataLoginException;

    /**
     * User logout from the server.
     */
    void userLogout(String username);

    /**
     * ThirdParty logout from the server.
     */
    void thirdPartyLogout(String email);

    /**
     * User sign up on server
     */
    void userSignUp(String ssn, String username, String password, String firstName, String lastName, String birthDay, String birthCity, String birthNation) throws UserAlreadySignUpException;

    /**
     * Third party sign up on server.
     */
    void thirdPartySignUp(String ssn, String email, String password, String firstName, String lastName, String birthDay, String birthCity, String birthNation) throws UserAlreadySignUpException;

    /**
     * Company sign up on server.
     */
    void companySignUp(String companyName, String email, String password, String address, String dunsNumber) throws UserAlreadySignUpException;

}
