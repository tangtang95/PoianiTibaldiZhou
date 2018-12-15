package com.trackme.trackmeapplication.groupRequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupRequestBodyActivity extends AppCompatActivity {

    @BindView(R.id.spinnerAggregator)
    protected Spinner spinnerAggregator;
    @BindView(R.id.spinnerType)
    protected Spinner spinnerType;
    @BindView(R.id.spinnerColumn)
    protected Spinner spinnerColumn;
    @BindView(R.id.spinnerOperator)
    protected Spinner spinnerOperator;
    @BindView(R.id.imageViewSend)
    protected ImageView send;
    @BindView(R.id.imageViewPlus)
    protected ImageView plus;
    @BindView(R.id.imageViewMin)
    protected ImageView remove;
    @BindView(R.id.editTextNumber)
    protected EditText number;
    @BindView(R.id.textViewFilterData)
    protected TextView filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_request_form);
        ButterKnife.bind(this);

        List<String> aggregators = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        aggregators.add("A");
        aggregators.add("B");
        aggregators.add("C");
        types.add("A");
        types.add("B");
        columns.add("ciao");
        columns.add("volpe");
        columns.add("gino");
        operators.add(">");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aggregators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAggregator.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, columns);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColumn.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperator.setAdapter(adapter3);


    }

    @OnClick(R.id.imageViewPlus)
    public void onPlusButtonClick() {
        String column = spinnerColumn.getSelectedItem().toString();
        String operator = spinnerOperator.getSelectedItem().toString();
        String n = number.getText().toString();
        if (filter.getText().toString().isEmpty()) {
            String f = column + operator + n;
            filter.setText(f);
        } else {
            String f = filter.getText().toString() + "+" + column + operator + n;
            filter.setText(f);
        }

    }

    @OnClick(R.id.imageViewMin)
    public void onRemoveButtonClick() {
        String f = filter.getText().toString();
        StringBuilder newF = new StringBuilder();
        if (!f.isEmpty()) {
            String[] filterItem =  f.split("\\+");
            for (int i = 0; i < filterItem.length - 1; i++) {
                newF.append(filterItem[i]);
                if (i != filterItem.length - 2)
                    newF.append("+");
            }
        }
        filter.setText(newF.toString());
    }

    @OnClick(R.id.imageViewSend)
    public void onSendButtonClick() {
        if (filter.getText().toString().isEmpty())
            Toast.makeText(this,"No field must be empty", Toast.LENGTH_LONG).show();
        else {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            GroupRequestItem groupRequestItem = new GroupRequestItem("",
                    dateFormat.format(date),
                    spinnerAggregator.getSelectedItem().toString(),
                    spinnerType.getSelectedItem().toString(),
                    filter.getText().toString());
            /*TODO*/
            finish();
        }
    }

}
