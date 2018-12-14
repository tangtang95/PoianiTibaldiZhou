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

public class CompanyRegisterFragment extends Fragment {

    @BindView(R.id.register_company_name)
    protected EditText companyName;
    @BindView(R.id.register_mail)
    protected EditText mail;
    @BindView(R.id.register_password)
    protected EditText password;
    @BindView(R.id.register_address)
    protected EditText address;
    @BindView(R.id.register_duns_number)
    protected EditText dunsNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View companyRegisterFragment = inflater.inflate(R.layout.fragment_company_register, container, false);
        ButterKnife.bind(this, companyRegisterFragment);
        return companyRegisterFragment;
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (checkConstraintOnData()) {
            /*TODO*/
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private boolean checkConstraintOnData() {
        if (companyName.getText().toString().isEmpty() ||
                mail.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() ||
                dunsNumber.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"No field must be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mail.getText().toString().matches(Constant.E_MAIL_PATTERN)) {
            mail.setError("E-mail is not valid");
            return false;
        }
        return true;
    }
}
