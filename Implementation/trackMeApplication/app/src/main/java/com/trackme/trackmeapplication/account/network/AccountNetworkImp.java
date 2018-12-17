package com.trackme.trackmeapplication.account.network;

public class AccountNetworkImp implements AccountNetworkInterface {

    private static AccountNetworkImp instance = null;

    private AccountNetworkImp() {}

    public static AccountNetworkImp getInstance() {
        if(instance == null)
            instance = new AccountNetworkImp();
        return instance;
    }

    @Override
    public void userLogin() {

    }

    @Override
    public void thirdPartyLogin() {

    }

    @Override
    public void userLogout() {

    }

    @Override
    public void thirdPartyLogout() {

    }

    @Override
    public void userSignUp() {

    }

    @Override
    public void thirdPartySignUp() {

    }
}
