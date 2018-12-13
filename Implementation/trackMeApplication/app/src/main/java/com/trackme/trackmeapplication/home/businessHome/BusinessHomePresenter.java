package com.trackme.trackmeapplication.home.businessHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenterImpl;

public class BusinessHomePresenter extends BasePresenterImpl<BusinessHomeContract.BusinessHomeView>
    implements BusinessHomeContract.BusinassHomePresenter {
    @Override
    public void onProfileSelected() {
        mView.navigateToBusinessProfile();
    }

    @Override
    public void onSettingsSelected() {
        mView.navigateToBusinessSettings();
    }

    @Override
    public void onLogoutSelected() {
        mView.navigateToUserLogin();
    }
}
