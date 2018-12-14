package com.trackme.trackmeapplication.account.login;

import android.text.TextUtils;

import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;
import com.trackme.trackmeapplication.baseUtility.Constant;

public class LoginDelegate extends BaseActivityDelegate<LoginContract.LoginView,LoginPresenter> {

    public void userLogin(final String username, final String password) {
        /*TODO*/
        if (username.isEmpty() || password.isEmpty())
            mPresenter.onLoginError();
        else
            mPresenter.onLoginSuccess();
    }

    public void businessLogin(final String mail, final String password) {
        /*TODO*/
        if (mail.isEmpty() || password.isEmpty())
            mPresenter.onLoginError();
        else
            if (!isValidEmail(mail))
                mPresenter.onMailError();
            else
                mPresenter.onLoginSuccess();
    }

    private boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && target.matches(Constant.E_MAIL_PATTERN));
    }
}
