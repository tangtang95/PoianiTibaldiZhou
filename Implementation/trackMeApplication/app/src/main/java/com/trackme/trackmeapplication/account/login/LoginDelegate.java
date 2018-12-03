package com.trackme.trackmeapplication.account.login;

import android.text.TextUtils;
import android.util.Patterns;

import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;

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

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
