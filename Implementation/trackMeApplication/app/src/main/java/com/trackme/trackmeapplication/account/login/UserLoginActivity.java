package com.trackme.trackmeapplication.account.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseAlertDialog;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.home.businessHome.BusinessHomeActivity;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.trackme.trackmeapplication.service.position.UserLocationListener.INITIAL_PERMS;
import static com.trackme.trackmeapplication.service.position.UserLocationListener.INITIAL_REQUEST;

/**
 * UserLoginActivity extends the abstract class LoginActivity and it allows to the user to
 * login into the application. It is the first Activity that starts when the user install
 * the apk on his mobile device.
 *
 * @author Mattia Tibaldi
 * @see LoginActivity
 **/
public class UserLoginActivity extends LoginActivity {

    @BindView(R.id.editTextUser)
    protected EditText username;

    /**
     * True if the broadcast receiver is registered, false otherwise.
     */
    private boolean isRegister;

    /**
     * it manages the interaction with other activity like businessLoginActivity. it is used mainly
     * for finishing an activity from an other.
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        /**
         * It stops the activity when the action sent is "finish_activity"
         *
         * @param arg0 the sender intent.
         * @param intent action to perform.
         */
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Constant.FINISH_ACTION)) {
                unregisterReceiver(this);
                isRegister = false;
                finish();
            } else {
                BaseAlertDialog alertDialog = new BaseAlertDialog(
                        arg0,
                        getString(R.string.broadcast_receiver_error),
                        getString(R.string.fatal_error_title));
                alertDialog.show();
            }
        }
    };

    /**
     * Method call when this activity is created. It checks the past user session for verifying
     * if the user has already logged and it chooses the best activity to start.
     *
     * @param savedInstanceState last saved instant state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        sp = getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME,MODE_PRIVATE);
        isRegister = false;

        if (sp.getBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, false)) {
            navigateToHome();
        } else if (sp.getBoolean(Constant.BUSINESS_LOGGED_BOOLEAN_VALUE_KEY, false))
            navigateToBusinessHome();

        if (!canAccessLocation())
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
    }

    /**
     * Check if the permission of getting the GPS data.
     *
     * @return true if the access to the GPS is allowed, false otherwise
     */
    private boolean canAccessLocation() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
        if (isRegister)
            unregisterReceiver(broadcastReceiver);
        finish();
    }


    /**
     * It starts a new BusinessHomeActivity and it shows it to the user. At the end it stops this
     * activity.
     */
    private void navigateToBusinessHome() {
        Intent intent = new Intent(this, BusinessHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveUserSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.USER_LOGGED_BOOLEAN_VALUE_KEY, true);
        editor.putString(Constant.SD_USERNAME_DATA_KEY, username.getText().toString());
        editor.apply();
    }

    @Override
    public void navigateToBusinessLogin() {
        Intent intent = new Intent(this, BusinessLoginActivity.class);
        if (!isRegister) {
            registerReceiver(broadcastReceiver, new IntentFilter(Constant.FINISH_ACTION));
            isRegister = true;
        }
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

    /**
     * It handles the user login button click event and it delegate the validation to the
     * LoginDelegate.
     */
    @OnClick(R.id.userLoginButton)
    public void onUserLoginButtonClick() {
        mDelegate.userLogin(username.getText().toString(), password.getText().toString());
    }

    /**
     * It handles the third party login click event.
     */
    @OnClick(R.id.textViewThirdPartyLogin)
    public void onTextViewThirdPartyLoginClick() {
        mPresenter.businessLogin();
    }
}
