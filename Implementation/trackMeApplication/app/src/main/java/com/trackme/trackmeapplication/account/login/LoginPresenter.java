package com.trackme.trackmeapplication.account.login;

import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

public class LoginPresenter extends BasePresenterImpl<LoginContract.LoginView> implements
        LoginContract.LoginPresenter {

    @Override
    public void register() {
        mView.navigateToRegister();
    }

    @Override
    public void businessLogin() {
        mView.navigateToBusinessLogin();
    }

    @Override
    public void onLoginError() {
        mView.setLoginError();
    }

    @Override
    public void onLoginSuccess() {
        mView.navigateToHome();
    }

    @Override
    public void onMailError() {
        mView.setMailError();
    }

}
