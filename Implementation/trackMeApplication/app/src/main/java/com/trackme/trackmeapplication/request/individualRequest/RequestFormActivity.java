package com.trackme.trackmeapplication.request.individualRequest;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkIInterface;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkImp;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Request form activity shows to the third party a form for compiling and sending an individual
 * request to a target user.
 *
 * @author Mattia Tibaldi
 */
public class RequestFormActivity extends AppCompatActivity {

    @BindView(R.id.editTextSnn)
    protected EditText ssn;
    @BindView(R.id.editTextStartDate)
    protected TextView startDate;
    @BindView(R.id.editTextEndDate)
    protected TextView endDate;
    @BindView(R.id.editTextMotive)
    protected EditText motive;

    private DatePickerDialog.OnDateSetListener onDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);
        ButterKnife.bind(this);

        onDateSetListenerStart = (datePicker, year, month, day) -> {
            month++;
            String date = month + "/" + day + "/" + year;
            startDate.setText(date);
        };

        onDateSetListenerEnd = (datePicker, year, month, day) -> {
            month++;
            String date = month + "/" + day + "/" + year;
            endDate.setText(date);
        };

    }

    /**
     * Handle the send button click event.
     */
    @OnClick(R.id.imageViewSend)
    public void onSendButtonClick() {
        if (checkConstraintOnData()) {

            AccountNetworkInterface accountNetwork = AccountNetworkImp.getInstance();
            IndividualRequestNetworkIInterface individualRequestNetwork = IndividualRequestNetworkImp.getInstance();
            try {
                individualRequestNetwork.send(new RequestItem(
                        ssn.getText().toString(),
                        accountNetwork.getThirdParty().getName(),
                        startDate.getText().toString(),
                        endDate.getText().toString(),
                        motive.getText().toString()
                ));
            } catch (UserNotFoundException e) {
                Toast.makeText(this, getString(R.string.impossible_to_find_user_detail), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    /**
     * It handles the startDate click event and it shows to the user a calendar for
     * selecting the date.
     */
    @OnClick(R.id.editTextStartDate)
    public void onStarDateClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListenerStart,
                year, month, day);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * It handles the endDate click event and it shows to the user a calendar for
     * selecting the date.
     */
    @OnClick({R.id.editTextEndDate})
    public void onEndDateClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListenerEnd,
                year, month, day);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * Control if all the data insert by the user in the form are valid.
     *
     * @return true if the data insert are acceptable, false otherwise.
     */
    private boolean checkConstraintOnData() {
        if (ssn.getText().toString().isEmpty() ||
                startDate.getText().toString().isEmpty() ||
                endDate.getText().toString().isEmpty() ||
                motive.getText().toString().isEmpty()) {
            Toast.makeText(this,getString(R.string.no_field_must_be_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ssn.getText().toString().matches(Constant.SSN_PATTERN)) {
            ssn.setError(getString(R.string.ssn_is_not_valid));
            return false;
        }
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date start;
        Date end;
        try {
            start = dateFormat.parse(startDate.getText().toString());
            end = dateFormat.parse(endDate.getText().toString());
            if (end.before(start)) {
                endDate.setError(getString(R.string.date_before_error));
                return false;
            }
        } catch (ParseException e) {
           return false;
        }
        return true;
    }
}
