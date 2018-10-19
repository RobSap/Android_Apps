package rsapping.edu.uoregon.tideprediction;


import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.content.Intent;

/**
 * Created by Alphadog1939 on 7/9/16.
 */
public class FirstActivity  extends AppCompatActivity  implements View.OnClickListener , AdapterView.OnItemSelectedListener {


    //public Spinner location;
    public DatePicker date;
    public Spinner locationSpinner;
    public Button button;
    SimpleCursorAdapter adapter = null;
    String locationSelection = "Florence";

    //Create
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //Initialize the button
        button = (Button) findViewById(R.id.showTideButton);
        button.setOnClickListener(this);

        //Init date picker
        date = (DatePicker) findViewById(R.id.datePicker);

        //Set max and min for this year
        date.setMaxDate(1483228799000L);
        date.setMinDate(1451696759000L);


        // Set up location selection spinner
        locationSpinner = (Spinner)findViewById(R.id.spinner);
        locationSpinner.setOnItemSelectedListener(this);

    }

    //Allow user to click on the button
    public void onClick(View v) {

        if (v.getId() == R.id.showTideButton) {

            //Create the intent
            Intent myIntent = new Intent(this, SecondActivity.class);

            //Pass over the location and the day/month user selected
            myIntent.putExtra("location", locationSpinner.getSelectedItem().toString() ); //Optional parameters
            myIntent.putExtra("day", String.valueOf(date.getDayOfMonth())); //
            myIntent.putExtra("month", String.valueOf(date.getMonth())); //
            //Start activity
            startActivity(myIntent);
        }

    }



    //This lets the User Select which location
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (position){
            case 0:
                locationSelection = "Florence";
                break;
            case 1:
                locationSelection = "Coos";
                break;
            case 2:
                locationSelection = "Depoe";
                break;
            default:
                locationSelection = "Florence";
                break;

        }

    }

    //If they don't select, florence is the default, but it throws and error without this below
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
