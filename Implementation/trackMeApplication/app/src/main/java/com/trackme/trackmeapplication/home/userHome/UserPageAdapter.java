package com.trackme.trackmeapplication.home.userHome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trackme.trackmeapplication.idividualRequest.IndividualMessageFragment;

public class UserPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    UserPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserHomeFragment();
            case 1:
                return new UserHistoryFragment();
            case 2:
                return new IndividualMessageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
