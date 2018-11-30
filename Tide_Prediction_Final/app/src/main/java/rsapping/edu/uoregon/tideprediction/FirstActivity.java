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
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        //Set max and min for this year - took this out to let user select any date they want
        //date.setMaxDate(1483228799000L);
        //date.setMinDate(1451696759000L);


        // Set up location selection spinner
        locationSpinner = (Spinner)findViewById(R.id.dateSpinner);
        locationSpinner.setOnItemSelectedListener(this);

    }

    //Allow user to click on the button
    public void onClick(View v) {

        if (v.getId() == R.id.showTideButton) {

            //Create the intent
            Intent myIntent = new Intent(this, SecondActivity.class);

            //Use calender to get the next day also
            Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
            calendar.add(Calendar.DAY_OF_YEAR, 1);


            //Pass over the location and the day/month user selected
            myIntent.putExtra("location", locationSelection); //Optional parameters

            //Pass the first day
            myIntent.putExtra("dayBefore", String.valueOf(date.getDayOfMonth())); //
            myIntent.putExtra("monthBefore", String.valueOf(date.getMonth()+1)); //
            myIntent.putExtra("yearBefore", String.valueOf(date.getYear())); //
            //myIntent.putExtra("dayOfWeekBefore", String.valueOf(dayOfWeek)); //

            //Pass the day after the first day
            myIntent.putExtra("dayAfter", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))); //
            myIntent.putExtra("monthAfter", Integer.toString(calendar.get(Calendar.MONTH)+1)); //
            myIntent.putExtra("yearAfter", Integer.toString(calendar.get(Calendar.YEAR))); //
           // myIntent.putExtra("dayOfWeekAfter", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK))); //

            //Start activity
            startActivity(myIntent);
        }

    }



    //This lets the User Select which location, this sets the location
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (position){
            case 0:
                //Umpqua River Entrance, Half Moon Bay
                locationSelection = "9433445";
                break;
            case 1://Florence USCG Pier
                locationSelection = "9434098";
                break;
            case 2://Coos Bay
                locationSelection = "9432780";
                break;
            case 3://South Beach
                //Umpqua River Entrance, Half Moon Bay
                locationSelection = "9435380";
                break;
            case 4://Wiser Point
                locationSelection = "9435308";
                break;
            case 5:// Garibaldi
                locationSelection = "9437540";
                break;
            case 6://Dick point
                locationSelection = "9437381";
                break;
            case 7://Gold Beach
                locationSelection = "9431011";
                break;
            case 8://Port Orford
                locationSelection = "9431647";
                break;
            case 9://Waldport
                locationSelection = "9434939";
                break;
            case 10://Deopo
                locationSelection = "9435827";
                break;
            case 11: //Cascade Head, Salmon River
                locationSelection = "9436381";
                break;
            default: //Coos bay default if some error or fails
                locationSelection = "9432780";
                break;
        }

    }

    //If they don't select, florence is the default, but it throws and error without this below
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
