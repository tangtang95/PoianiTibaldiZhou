package com.trackme.trackmeapplication.home.userHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

public class UserHomePresenter extends BasePresenterImpl<
        UserHomeContract.UserHomeView> implements UserHomeContract.UserHomePresenter {

    @Override
    public void onMessageSelected() {
        mView.showIndividualMessageFragment();
    }

    @Override
    public void onProfileSelected() {
        mView.showProfileFragment();
    }

    @Override
    public void onSettingsSelected() {
        mView.showSettingsFragment();
    }
}
