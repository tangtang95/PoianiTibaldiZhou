package com.trackme.trackmeapplication.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.trackme.trackmeapplication.HomeActivity;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginContract.LoginPresenter>
        implements LoginContract.LoginView {

    @BindView(R.id.progressBar) protected ProgressBar progressBar;
    @BindView(R.id.editTextUser) protected EditText username;
    @BindView(R.id.editTextPass) protected EditText password;

    @NonNull
    @Override
    protected LoginContract.LoginPresenter getPresenterInstance() {
        return new LoginPresenter( new LoginInteracting());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToRegister() {
        /*TODO*/
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick() {
        mPresenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.textViewRegister)
    public void onTextViewRegisterClick() {
        mPresenter.register();
    }

}
