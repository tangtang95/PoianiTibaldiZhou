package com.trackme.trackmeapplication.account.login;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

class LoginContract {

    interface LoginView extends BaseView {

        void navigateToHome();

        void saveUserSession();

        void navigateToRegister();

        void navigateToBusinessLogin();

        void setLoginError();

        void setMailError();
    }

    interface LoginPresenter extends BasePresenter<LoginView> {

        void register();

        void businessLogin();

        void onLoginError();

        void onLoginSuccess();

        void onMailError();

    }
}
