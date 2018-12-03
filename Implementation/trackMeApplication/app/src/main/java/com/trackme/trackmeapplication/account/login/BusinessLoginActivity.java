package com.trackme.trackmeapplication.account.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.trackme.trackmeapplication.UserHomeActivity;
import com.trackme.trackmeapplication.R;

import butterknife.BindView;
import butterknife.OnClick;

public class BusinessLoginActivity extends LoginActivity{

    @BindView(R.id.editTextMail) protected EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        Intent finishIntent = new Intent("finish_activity");
        sendBroadcast(finishIntent);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToBusinessLogin() {
        throw new IllegalStateException();
    }

    @Override
    public void setLoginError() {
        password.setError(getString(R.string.business_login_error));
    }

    @Override
    public void setMailError() {
        mail.setError(getString(R.string.email_is_not_valid));
    }

    @OnClick(R.id.businessLoginButton)
    public void onBusinessLoginButtonClick() {
        mDelegate.businessLogin(mail.getText().toString(), password.getText().toString());
    }

}
