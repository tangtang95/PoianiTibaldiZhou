package com.trackme.trackmeapplication.account;

import android.content.res.Resources;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

public class LoginPresenter extends BasePresenterImpl<LoginContract.LoginView> implements
        LoginContract.LoginPresenter {

    private LoginInteracting loginInteracting;

    LoginPresenter(LoginInteracting loginInteracting) {
        this.loginInteracting = loginInteracting;
    }

    @Override
    public void validateCredentials(String username, String password) {
        loginInteracting.login(username, password, this);
    }

    @Override
    public void register() {
        mView.navigateToRegister();
    }

    @Override
    public void onLoginError() {
        mView.showMessage(Resources.getSystem().getString(R.string.login_error));
    }

    @Override
    public void onLoginSuccess() {
        mView.navigateToHome();
    }

}
