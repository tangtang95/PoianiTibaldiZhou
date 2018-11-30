package com.trackme.trackmeapplication.account;

import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;

public class LoginDelegate extends BaseActivityDelegate<LoginContract.LoginView,LoginPresenter> {

    public void userLogin(final String username, final String password) {
        /*TODO*/
        mPresenter.onLoginSuccess();
    }

    public void businessLogin(final String mail, final String password) {
        /*TODO*/
        mPresenter.onLoginSuccess();
    }
}
