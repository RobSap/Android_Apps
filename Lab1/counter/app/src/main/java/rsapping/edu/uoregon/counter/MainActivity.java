package rsapping.edu.uoregon.counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {


    //Declare buttons
    private Button resetButton;
    private Button incrementButton;

    //Declare the text ot be displayed
    private TextView countTextView;

    //Initialize value for countvalue to pass on
    private float countValue = 0.0f;

    //Make it so I can save preferences to rotate and reload
    private SharedPreferences savedValues;


    //Used to start the app "Load initially"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find all the buttons and text fields
        countTextView = (TextView) findViewById(R.id.countTextView);
        incrementButton = (Button) findViewById(R.id.incrementButton);
        resetButton = (Button) findViewById(R.id.resetButton);



        //Sets up the two button listeners
        incrementButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        //Get any shared/saved preferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }


    //Save state
    @Override
    public void onPause() {
        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putFloat("countValue", countValue);
        editor.commit();

        super.onPause();

        // Log.d(TAG, "onPause executed");

    }

    //Load prevrious saved state, else set default
    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        countValue= savedValues.getFloat("countValue", 0.0f);

        updateAndDisplay(countValue);


    }


    //Used to update the count value
    public void updateAndDisplay(float updateValue) {

        if (updateValue == 1.0f) // to increment
            countValue=countValue+1.0f;
        else if (updateValue == 0.0f) //to reset
            countValue = 0.0f;
        else//for resume
            countValue = updateValue;

        NumberFormat num = NumberFormat.getNumberInstance();
        countTextView.setText(num.format(countValue));
    }

    //Detects clicks and passes correct value to function to update
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                updateAndDisplay(0.0f);
                break;
            case R.id.incrementButton:
                updateAndDisplay(1.0f);
                break;
        }
    }

}
