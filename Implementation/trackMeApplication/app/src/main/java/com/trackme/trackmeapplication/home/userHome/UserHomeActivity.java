package com.trackme.trackmeapplication.home.userHome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.login.UserLoginActivity;
import com.trackme.trackmeapplication.account.register.UserProfileActivity;
import com.trackme.trackmeapplication.baseUtility.BaseDelegationActivity;

import butterknife.BindView;

public class UserHomeActivity extends BaseDelegationActivity<
        UserHomeContract.UserHomeView,
        UserHomePresenter,
        UserHomeDelegate> implements UserHomeContract.UserHomeView {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private SharedPreferences sp;
    private String username;

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

        sp = getSharedPreferences("login", MODE_PRIVATE);
        username = sp.getString("username", null);
        super.onCreate(savedInstanceState);
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
    protected void onResume() {
        username = sp.getString("username", null);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            /*TODO*/
            sp.edit().putBoolean("user_logged", false).apply();
            navigateToUserLogin();
        }
        return true;
    }

    @Override
    public void navigateToUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToUserSettings() {
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToUserLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public UserHomeActivity getActivity() {
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
