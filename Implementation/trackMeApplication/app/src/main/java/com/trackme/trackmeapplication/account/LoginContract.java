package com.trackme.trackmeapplication.account;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

class LoginContract {

    interface LoginView extends BaseView {

        void navigateToHome();

        void navigateToRegister();
    }

    interface LoginPresenter extends BasePresenter<LoginView> {

        void validateCredentials(String username, String password);

        void register();

        void onLoginError();

        void onLoginSuccess();

    }
}
