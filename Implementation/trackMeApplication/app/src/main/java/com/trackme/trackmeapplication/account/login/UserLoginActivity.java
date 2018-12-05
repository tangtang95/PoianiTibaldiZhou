package com.trackme.trackmeapplication.account.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.trackme.trackmeapplication.home.businessHome.BusinessHomeActivity;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;
import com.trackme.trackmeapplication.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class UserLoginActivity extends LoginActivity {

    @BindView(R.id.editTextUser)
    protected EditText username;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (Objects.requireNonNull(action).equals("finish_activity")) {
                finish();
                unregisterReceiver(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        sp = getSharedPreferences("login",MODE_PRIVATE);

        if (sp.getBoolean("user_logged", false)) {
            navigateToHome();
        } else if (sp.getBoolean("business_logged", false))
            navigateToBusinessHome();

    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
        finish();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception ignored) { }
    }


    private void navigateToBusinessHome() {
        Intent intent = new Intent(this, BusinessHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveUserSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("user_logged", true);
        editor.putString("username", username.getText().toString());
        editor.apply();
    }

    @Override
    public void navigateToBusinessLogin() {
        Intent intent = new Intent(this, BusinessLoginActivity.class);
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
        startActivity(intent);
    }

    @Override
    public void setLoginError() {
        password.setError(getString(R.string.user_login_error));
    }

    @Override
    public void setMailError() {
        throw new IllegalStateException();
    }

    @OnClick(R.id.userLoginButton)
    public void onUserLoginButtonClick() {
        mDelegate.userLogin(username.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.textViewThirdPartyLogin)
    public void onTextViewThirdPartyLoginClick() {
        mPresenter.businessLogin();
    }

}
