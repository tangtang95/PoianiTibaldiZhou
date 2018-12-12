package com.trackme.trackmeapplication.account.register;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegisterPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    RegisterPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserRegisterFragment();
            case 1:
                return new ThirdPartyRegisterFragment();
            case 2:
                return new CompanyRegisterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
