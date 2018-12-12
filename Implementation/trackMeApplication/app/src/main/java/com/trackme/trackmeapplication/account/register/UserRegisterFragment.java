package com.trackme.trackmeapplication.account.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserRegisterFragment extends Fragment {

    @BindView(R.id.register_ssn)
    protected EditText ssn;
    @BindView(R.id.register_username)
    protected EditText username;
    @BindView(R.id.register_password)
    protected EditText password;
    @BindView(R.id.register_first_name)
    protected EditText firstName;
    @BindView(R.id.register_last_name)
    protected EditText lastName;
    @BindView(R.id.register_birth_day)
    protected EditText birthDay;
    @BindView(R.id.register_birth_city)
    protected EditText birthCity;
    @BindView(R.id.register_birth_nation)
    protected EditText birthNation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userRegisterFragment =  inflater.inflate(R.layout.fragment_user_register, container, false);
        ButterKnife.bind(this, userRegisterFragment);
        return userRegisterFragment;
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (checkConstraintOnData()) {
            /*TODO*/
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private boolean checkConstraintOnData() {
        if (ssn.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                firstName.getText().toString().isEmpty() ||
                lastName.getText().toString().isEmpty() ||
                birthDay.getText().toString().isEmpty() ||
                birthCity.getText().toString().isEmpty() ||
                birthNation.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"No field must be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ssn.getText().toString().matches(Constant.SSN_PATTERN)) {
            ssn.setError("Ssn is not valid");
            return false;
        }
        if (username.getText().toString().length() > 30) {
            username.setError("Username is too long");
            return false;
        }
        if (!birthDay.getText().toString().matches(Constant.DATE_PATTERN)) {
            ssn.setError("Date is not valid");
            return false;
        }
        return true;
    }
}
