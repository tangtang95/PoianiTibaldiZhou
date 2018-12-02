package com.trackme.trackmeapplication.baseUtility;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseDelegationActivityTest {

    private BaseDelegationActivity baseDelegationActivity;

    @Before
    public void setUp(){
        baseDelegationActivity = new BaseDelegationActivity() {
            @Override
            protected BaseActivityDelegate instantiateDelegateInstance() {
                return new BaseActivityDelegate(){};
            }

            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return new BasePresenterImpl();
            }
        };
    }

    @Test
    public void onCreate() {
        //baseDelegationActivity.onCreate(new Bundle());
        //assertNotNull(baseDelegationActivity.mDelegate);
    }

    @Test
    public void onDestroy() {
        //baseDelegationActivity.onCreate(null);
        //baseDelegationActivity.onDestroy();
        //assertNotNull(baseDelegationActivity.mDelegate.getmUnBinder());
    }
}