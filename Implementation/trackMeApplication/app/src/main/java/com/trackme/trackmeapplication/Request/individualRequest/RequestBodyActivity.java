package com.trackme.trackmeapplication.Request.individualRequest;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestBodyActivity extends AppCompatActivity {

    @BindView(R.id.textViewThirdPartyName)
    protected TextView thirdPartyName;
    @BindView(R.id.textViewRequestBodyPeriod)
    protected TextView period;
    @BindView(R.id.textViewRequestBodyMotive)
    protected TextView motive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_body);

        RequestItem requestItem = (RequestItem) getIntent().getSerializableExtra("item");

        ButterKnife.bind(this);

        thirdPartyName.setText(requestItem.getThirdPartyName());
        String s = requestItem.getStartDate() + " to " + requestItem.getEndDate();
        period.setText(s);
        motive.setText(requestItem.getMotive());
    }

    @OnClick(R.id.requestBodyAccept)
    public void onRequestAcceptClick() {
        /*TODO*/
        finish();

    }

    @OnClick(R.id.requestBodyRefuse)
    public void onRequestRefuseClick() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    /*TODO*/
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    /*TODO*/
                    finish();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to block this third party customer?").
                setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
