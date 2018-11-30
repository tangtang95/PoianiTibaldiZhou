package com.trackme.trackmeapplication.account;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

class LoginContract {

    interface LoginView extends BaseView {

        void navigateToHome();

        void navigateToRegister();

        void navigateToBusinessLogin();

        void setLoginError();
    }

    interface LoginPresenter extends BasePresenter<LoginView> {

        void register();

        void businessLogin();

        void onLoginError();

        void onLoginSuccess();

    }
}
