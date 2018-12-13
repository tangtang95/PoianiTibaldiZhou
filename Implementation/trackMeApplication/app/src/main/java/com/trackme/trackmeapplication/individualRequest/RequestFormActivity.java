package com.trackme.trackmeapplication.individualRequest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestFormActivity extends AppCompatActivity {

    @BindView(R.id.editTextSnn)
    protected EditText ssn;
    @BindView(R.id.editTextStartDate)
    protected EditText startDate;
    @BindView(R.id.editTextEndDate)
    protected EditText endDate;
    @BindView(R.id.editTextMotive)
    protected EditText motive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.imageViewSend)
    public void onSendButtonClick() {
        if (checkConstraintOnData()) {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            String mail = sp.getString("email", null);
            /*TODO*/
            finish();
        }
    }

    private boolean checkConstraintOnData() {
        if (ssn.getText().toString().isEmpty() ||
                startDate.getText().toString().isEmpty() ||
                endDate.getText().toString().isEmpty() ||
                motive.getText().toString().isEmpty()) {
            Toast.makeText(this,"No field must be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ssn.getText().toString().matches(Constant.SSN_PATTERN)) {
            ssn.setError("Ssn is not valid");
            return false;
        }
        if (startDate.getText().toString().matches(Constant.DATE_PATTERN)) {
            startDate.setError("Date is not valid");
            return false;
        }
        if (endDate.getText().toString().matches(Constant.DATE_PATTERN)) {
            endDate.setError("Date is not valid");
            return false;
        }
        return true;
    }
}
