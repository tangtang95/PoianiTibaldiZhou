package com.trackme.trackmeapplication.home.userHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

public class UserHomeContract {

    interface UserHomeView extends BaseView {

        void showIndividualMessageFragment();

        void showProfileFragment();

        void showSettingsFragment();

        UserHomeActivity getActivity();

    }

    interface UserHomePresenter extends BasePresenter<UserHomeView> {

        void onMessageSelected();

        void onProfileSelected();

        void onSettingsSelected();

    }
}
