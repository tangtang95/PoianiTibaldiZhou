package com.trackme.trackmeapplication.account;

public class LoginInteracting {

    public void login(final String username, final String password,
                      final LoginContract.LoginPresenter loginPresenter) {
        loginPresenter.onLoginSuccess();
    }
}
