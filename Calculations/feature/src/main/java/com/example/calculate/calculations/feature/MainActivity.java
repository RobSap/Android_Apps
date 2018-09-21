package com.example.calculate.calculations.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button results=(Button) findViewById(R.id.calculate_button);
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText mortgage_value = (EditText) findViewById(R.id.mortgage_value);

                EditText interest_value = (EditText) findViewById(R.id.interest_value);

                EditText term_value = (EditText) findViewById(R.id.term_value);

                EditText yearly_tax_value = (EditText) findViewById(R.id.yearly_tax_value);

                TextView principal_value= (TextView) findViewById(R.id.principal_value);
                TextView total_pay_value= (TextView) findViewById(R.id.total_pay_value);
                TextView total_interest_value= (TextView) findViewById(R.id.total_interest_value);
                TextView total_taxes_value= (TextView) findViewById(R.id.total_taxes_value);

                float i = (Float.valueOf(interest_value.getText().toString()) /12);

                float temp =  Float.valueOf(mortgage_value.getText().toString());
                float line = 1.0f +i;
                int n = 12*Integer.parseInt(term_value.getText().toString());

                float top =1.0f;
                for (int j = 0; j < n ; j++)
                {

                        top = (top )* line ;

                }

                float bottom = top  -1;
                top = top * i;

                float yearly_taxes = ((Float.valueOf(yearly_tax_value.getText().toString())));
                float total_taxes = yearly_taxes * Integer.parseInt(term_value.getText().toString());
                float monthly_taxes = total_taxes / n;

                float monthly = ((top / bottom) * temp ) + monthly_taxes ;
                float total_paid_monthly  =monthly *n;

                float interest_paid = (((top / bottom) * temp ) *n) - temp;


                principal_value.setText(String.format("%.2f",monthly));

                total_pay_value.setText(String.format("%.2f",total_paid_monthly));

                total_interest_value.setText(String.format("%.2f",interest_paid));

                total_taxes_value.setText(String.format("%.2f",total_taxes));
            }


        });
    }
}
