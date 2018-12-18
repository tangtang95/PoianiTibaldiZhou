package com.trackme.trackmeapplication.account.network;

import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;

public class AccountNetworkImp implements AccountNetworkInterface {

    private static AccountNetworkImp instance = null;

    private AccountNetworkImp() {}

    public static AccountNetworkImp getInstance() {
        if(instance == null)
            instance = new AccountNetworkImp();
        return instance;
    }


    @Override
    public void userLogin(String username, String password) throws InvalidDataLoginException {

    }

    @Override
    public void thirdPartyLogin(String email, String password) throws InvalidDataLoginException {

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
}
