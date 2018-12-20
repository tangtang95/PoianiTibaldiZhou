package com.trackme.trackmeapplication.request.individualRequest;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkIInterface;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkImp;
import com.trackme.trackmeapplication.baseUtility.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Request body class shows to the user the body of the request message and it allows user to accept
 * or refuse the request.
 *
 * @author Mattia Tibaldi
 */
public class RequestBodyActivity extends AppCompatActivity {

    @BindView(R.id.textViewThirdPartyName)
    protected TextView thirdPartyName;
    @BindView(R.id.textViewRequestBodyPeriod)
    protected TextView period;
    @BindView(R.id.textViewRequestBodyMotive)
    protected TextView motive;

    IndividualRequestNetworkIInterface individualrequestNetwork = IndividualRequestNetworkImp.getInstance();
    RequestItem requestItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_body);

        requestItem = (RequestItem) getIntent().getSerializableExtra(Constant.SD_INDIVIDUAL_REQUEST_KEY);

        ButterKnife.bind(this);

        thirdPartyName.setText(requestItem.getThirdPartyName());
        String s = requestItem.getStartDate() + " to " + requestItem.getEndDate();
        period.setText(s);
        motive.setText(requestItem.getMotive());
    }

    /**
     * Handle the accept button click event.
     */
    @OnClick(R.id.requestBodyAccept)
    public void onRequestAcceptClick() {
        individualrequestNetwork.acceptIndividualRequest(requestItem.getID());
        finish();

    }

    /**
     * Handle the refuse button click event.
     */
    @OnClick(R.id.requestBodyRefuse)
    public void onRequestRefuseClick() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    individualrequestNetwork.blockThirdPartyCustomer(requestItem.getEmail());
                    individualrequestNetwork.refuseIndividualRequest(requestItem.getID());
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    individualrequestNetwork.refuseIndividualRequest(requestItem.getID());
                    finish();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.block_third_party_message)).
                setPositiveButton(android.R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.no, dialogClickListener).show();
    }
}
