package com.trackme.trackmeapplication.home.userHome;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.register.ProfileFragment;
import com.trackme.trackmeapplication.baseUtility.BaseDelegationActivity;
import com.trackme.trackmeapplication.idividualRequest.IndividualMessageFragment;

import butterknife.BindView;

public class UserHomeActivity extends BaseDelegationActivity<
        UserHomeContract.UserHomeView,
        UserHomePresenter,
        UserHomeDelegate> implements UserHomeContract.UserHomeView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, UserHomeActivity.class);
    }

    @NonNull
    @Override
    protected UserHomePresenter getPresenterInstance() {
        return new UserHomePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_home);
        setSupportActionBar(toolbar);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new IndividualMessageFragment()).commit();
            mDelegate.setCheckedItem(R.id.nav_message);
        }
    }

    @Override
    protected UserHomeDelegate instantiateDelegateInstance() {
        return new UserHomeDelegate();
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.closeDrawer()){
            super.onBackPressed();
        }
    }

    @Override
    public void showIndividualMessageFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new IndividualMessageFragment()).commit();
    }

    @Override
    public void showProfileFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProfileFragment()).commit();
    }

    @Override
    public void showSettingsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingsFragment()).commit();
    }

    @Override
    public UserHomeActivity getActivity() {
        return this;
    }
}
