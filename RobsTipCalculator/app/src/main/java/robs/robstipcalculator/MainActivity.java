package robs.robstipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.style.UpdateAppearance;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity
implements TextView.OnEditorActionListener, OnClickListener{

    private TextView custom_percent_label;
    private TextView custom_percent_value;

    private TextView ten_percent_value;
    private TextView twelve_percent_value;
    private TextView fifteen_percent_value;
    private TextView eighteen_percent_value;
    private TextView twently_percent_value;


    private Button plus;
    private Button minus;
    private EditText bill_amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custom_percent_label= (TextView) findViewById(R.id.custom_percent_label);
        custom_percent_value= (TextView) findViewById(R.id.custom_percent_value);
        ten_percent_value = (TextView) findViewById(R.id.ten_percent_value);
        twelve_percent_value =(TextView) findViewById(R.id.twelve_percent_value);
        fifteen_percent_value =(TextView) findViewById(R.id.fifteen_percent_value);
        eighteen_percent_value = (TextView) findViewById(R.id.eighteen_percent_value);
        twently_percent_value =(TextView) findViewById(R.id.twently_percent_value);


        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);



        bill_amount = (EditText) findViewById(R.id.bill_amount_value);

        bill_amount.setOnEditorActionListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.minus:
                updateCustom(0);
                break;

            case R.id.plus:
                updateCustom(1);
                break;
        }

    }
    public void updateCustom(int flag){

        float i = (Float.valueOf(custom_percent_label.getText().toString()));
        float j = (Float.valueOf(bill_amount.getText().toString()));

        if (flag ==0)
        {
            custom_percent_label.setText(String.format("%.2f",i-1));
            custom_percent_value.setText(String.format("%.2f",j*((i-1)*0.01)));
        }
        else
        {

            custom_percent_label.setText(String.format("%.2f",i+1));
            custom_percent_value.setText(String.format("%.2f",j*((i+1)*0.01)));
        }

    }

    public void updateDisplay(){

        float i = (Float.valueOf(custom_percent_label.getText().toString()));
        float j = (Float.valueOf(bill_amount.getText().toString()));

        custom_percent_label.setText(String.format("%.2f",i));
        custom_percent_value.setText(String.format("%.2f",j*((i)*0.01)));


        ten_percent_value.setText(String.format("%.2f",j*.1));
        twelve_percent_value.setText(String.format("%.2f",j*.12));
        fifteen_percent_value.setText(String.format("%.2f",j*.15));
        eighteen_percent_value.setText(String.format("%.2f",j*.18));
        twently_percent_value.setText(String.format("%.2f",j*.20));

    }


    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {

        if (actionID == EditorInfo.IME_ACTION_DONE || actionID == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            updateDisplay();
            textView.clearFocus();
        }
        return false;
    }

}