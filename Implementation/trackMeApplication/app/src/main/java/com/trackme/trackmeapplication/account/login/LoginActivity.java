package com.trackme.trackmeapplication.account.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.register.RegisterActivity;
import com.trackme.trackmeapplication.baseUtility.BaseDelegationActivity;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class LoginActivity extends BaseDelegationActivity<
        LoginContract.LoginView,
        LoginPresenter,
        LoginDelegate>
        implements LoginContract.LoginView {

    @BindView(R.id.progressBar) protected ProgressBar progressBar;
    @BindView(R.id.editTextPass) protected EditText password;
    protected SharedPreferences sp;

    @NonNull
    @Override
    protected LoginPresenter getPresenterInstance() {
        return new LoginPresenter();
    }

    @Override
    protected LoginDelegate instantiateDelegateInstance() {
        return new LoginDelegate();
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
    public void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void setLoginError() {
        password.setError(getString(R.string.user_login_error));
    }

    @OnClick(R.id.textViewRegister)
    public void onTextViewRegisterClick() {
        mPresenter.register();
    }

}

