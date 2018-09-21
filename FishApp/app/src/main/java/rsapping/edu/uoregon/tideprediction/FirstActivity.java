package rsapping.edu.uoregon.tideprediction;


import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;

import java.util.Calendar;
import java.util.Map;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * Created by Alphadog1939 on 7/9/16.
 */
public class FirstActivity  extends AppCompatActivity  implements View.OnClickListener  {


    public Spinner locationSpinnerTidesCities;
    public Spinner locationSpinnerTidesStates;
    public Spinner locationSpinnerWeatherCities;
    public Spinner locationSpinnerWeatherStates;

    //public Spinner location;
    //public Button getGPSLocationButton;
    public Button coastalWeatherButton;
    public Button currentWeatherButton;
    public Button fiveDayWeatherButton;
    public Button currentTidesButton;
    public Button TwoDayForecastTidesButton;


    SimpleCursorAdapter adapter = null;
    String locationSelection = null;
    String locationSelection2 = null;
    private String locationName = null;
    private String locationName2 = null;
    Map<String, String> location_map = Locations.Oregon;


    //Create
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //Initialize the button
        //getGPSLocationButton = (Button) findViewById(R.id.getGPSLocation);
        TwoDayForecastTidesButton = (Button) findViewById(R.id.TwoDayForecastTides);
        coastalWeatherButton = (Button) findViewById(R.id.coastalWeather);
        fiveDayWeatherButton = (Button) findViewById(R.id.fiveDayWeather);
        currentWeatherButton = (Button) findViewById(R.id.currentWeather);
        currentTidesButton = (Button) findViewById(R.id.currentTides);

        //getGPSLocationButton.setOnClickListener(this);
        TwoDayForecastTidesButton.setOnClickListener(this);
        coastalWeatherButton.setOnClickListener(this);
        fiveDayWeatherButton.setOnClickListener(this);
        currentWeatherButton.setOnClickListener(this);
        currentTidesButton.setOnClickListener(this);

        // Set up location selection spinner
        locationSpinnerTidesCities = (Spinner)findViewById(R.id.dateSpinner);
        locationSpinnerTidesCities.setOnItemSelectedListener(new CountriesSpinnerClass());



        locationSpinnerWeatherStates = (Spinner)findViewById(R.id.dateSpinner2);



        locationSpinnerWeatherCities = (Spinner)findViewById(R.id.dateSpinner3);
        locationSpinnerWeatherCities.setOnItemSelectedListener(new CountriesSpinnerClass3());


        locationSpinnerTidesStates = (Spinner)findViewById(R.id.dateSpinner4);
        locationSpinnerTidesStates.setOnItemSelectedListener(new CountriesSpinnerClass4());


    }

    //Allow user to click on the button
    public void onClick(View v) {


        /*if (v.getId() == R.id.getGPSLocation) {


        }

         else */if (v.getId() == R.id.coastalWeather) {

            //Create the intent
            Intent myIntent2 = new Intent(this, CoastalWeatherActivity.class);

            //Use calender to get the next day also
            Calendar calendar = Calendar.getInstance();
            //Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            //Pass over the location and the day/month user selected
            myIntent2.putExtra("location_name", locationName2); //Optional parameters
            myIntent2.putExtra("location", locationSelection2); //Optional parameters

            //Pass the first day
            myIntent2.putExtra("dayBefore", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)-1)); //
            myIntent2.putExtra("monthBefore", String.valueOf(calendar.get(Calendar.MONTH)+1)); //
            myIntent2.putExtra("yearBefore", String.valueOf(calendar.get(Calendar.YEAR))); //
            //myIntent.putExtra("dayOfWeekBefore", String.valueOf(dayOfWeek)); //

            //Pass the day after the first day
            myIntent2.putExtra("dayAfter", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))); //
            myIntent2.putExtra("monthAfter", Integer.toString(calendar.get(Calendar.MONTH)+1)); //
            myIntent2.putExtra("yearAfter", Integer.toString(calendar.get(Calendar.YEAR))); //
           // myIntent.putExtra("dayOfWeekAfter", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK))); //

            //Start activity
            startActivity(myIntent2);
        }

            else if (v.getId() == R.id.currentWeather) {
            //Create the intent
            Intent myIntent10 = new Intent(this, WeatherActivity.class);
            startActivity(myIntent10);

        }
            else if (v.getId() == R.id.fiveDayWeather) {
        }
        else if (v.getId() == R.id.currentTides) {
            //Create the intent
            Intent myIntent5 = new Intent(this, TidesActivity.class);

            //Use calender to get the next day also
            Calendar calendar = Calendar.getInstance();
            //Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            //Pass over the location and the day/month user selected
            myIntent5.putExtra("location_name", locationName); //Optional parameters
            myIntent5.putExtra("location", locationSelection); //Optional parameters

            //Pass the first day
            myIntent5.putExtra("dayBefore", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)-1)); //
            myIntent5.putExtra("monthBefore", String.valueOf(calendar.get(Calendar.MONTH)+1)); //
            myIntent5.putExtra("yearBefore", String.valueOf(calendar.get(Calendar.YEAR))); //
            //myIntent.putExtra("dayOfWeekBefore", String.valueOf(dayOfWeek)); //

            //Pass the day after the first day
            myIntent5.putExtra("dayAfter", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)-1)); //
            myIntent5.putExtra("monthAfter", Integer.toString(calendar.get(Calendar.MONTH)+1)); //
            myIntent5.putExtra("yearAfter", Integer.toString(calendar.get(Calendar.YEAR))); //
            // myIntent.putExtra("dayOfWeekAfter", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK))); //

            //Start activity
            startActivity(myIntent5);

        }
        else if (v.getId() == R.id.TwoDayForecastTides) {

            //Create the intent
            Intent myIntent5 = new Intent(this, TidesActivity.class);

            //Use calender to get the next day also
            Calendar calendar = Calendar.getInstance();
            //Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            //Pass over the location and the day/month user selected
            myIntent5.putExtra("location_name", locationName); //Optional parameters
            myIntent5.putExtra("location", locationSelection); //Optional parameters

            //Pass the first day
            myIntent5.putExtra("dayBefore", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)-1)); //
            myIntent5.putExtra("monthBefore", String.valueOf(calendar.get(Calendar.MONTH)+1)); //
            myIntent5.putExtra("yearBefore", String.valueOf(calendar.get(Calendar.YEAR))); //
            //myIntent.putExtra("dayOfWeekBefore", String.valueOf(dayOfWeek)); //

            //Pass the day after the first day
            myIntent5.putExtra("dayAfter", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))); //
            myIntent5.putExtra("monthAfter", Integer.toString(calendar.get(Calendar.MONTH)+1)); //
            myIntent5.putExtra("yearAfter", Integer.toString(calendar.get(Calendar.YEAR))); //

            //Start activity
            startActivity(myIntent5);

        }

    }



    class CountriesSpinnerClass2 implements AdapterView.OnItemSelectedListener {
        //This lets the User Select which location, this sets the location
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        }
        //If they don't select, florence is the default, but it throws and error without this below
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }


    class CountriesSpinnerClass3 implements AdapterView.OnItemSelectedListener {
        //This lets the User Select which location, this sets the location
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            locationName2 = parent.getItemAtPosition(position).toString();
            locationSelection2 = location_map.get(locationName2);

        }
        //If they don't select, florence is the default, but it throws and error without this below
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }



    class CountriesSpinnerClass4 implements AdapterView.OnItemSelectedListener {
        //This lets the User Select which location, this sets the location
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


           //Onselect set adapter = parent.getItemAtPosition(position).toString();

        }
        //If they don't select, florence is the default, but it throws and error without this below
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    }

    class CountriesSpinnerClass implements AdapterView.OnItemSelectedListener {
        //This lets the User Select which location, this sets the location
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            locationName = parent.getItemAtPosition(position).toString();
            locationSelection = location_map.get(locationName);

        }
        //If they don't select, florence is the default, but it throws and error without this below
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }



}
