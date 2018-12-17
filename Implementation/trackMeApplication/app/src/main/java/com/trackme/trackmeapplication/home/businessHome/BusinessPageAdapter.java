package com.trackme.trackmeapplication.home.businessHome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trackme.trackmeapplication.Request.groupRequest.GroupRequestFragment;
import com.trackme.trackmeapplication.Request.individualRequest.IndividualMessageBusinessFragment;

public class BusinessPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public BusinessPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IndividualMessageBusinessFragment();
            case 1:
                return new GroupRequestFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
