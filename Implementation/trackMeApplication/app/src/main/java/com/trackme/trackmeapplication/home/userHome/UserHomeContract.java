package com.trackme.trackmeapplication.home.userHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

public class UserHomeContract {

    interface UserHomeView extends BaseView {

        void navigateToUserProfile();

        void navigateToUserSettings();

        void navigateToUserLogin();

        UserHomeActivity getActivity();

        String getUsername();

    }

    interface UserHomePresenter extends BasePresenter<UserHomeView> {

        void onProfileSelected();

        void onSettingsSelected();

    }
}
