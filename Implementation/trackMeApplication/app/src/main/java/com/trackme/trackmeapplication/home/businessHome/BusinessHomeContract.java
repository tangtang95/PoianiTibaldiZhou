package com.trackme.trackmeapplication.home.businessHome;

import com.trackme.trackmeapplication.baseUtility.BasePresenter;
import com.trackme.trackmeapplication.baseUtility.BaseView;

public class BusinessHomeContract {

    interface BusinessHomeView extends BaseView {

        void navigateToBusinessProfile();

        void navigateToBusinessSettings();

        void navigateToUserLogin();

        BusinessHomeActivity getActivity();

        String getMail();

    }

    interface BusinassHomePresenter extends BasePresenter<BusinessHomeView> {

        void onProfileSelected();

        void onSettingsSelected();

        void onLogoutSelected();

    }
}
