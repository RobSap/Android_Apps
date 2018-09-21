package rsapping.edu.uoregon.tideprediction;


import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;


public class CoastalWeatherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{



    String locationSelection = null;
    String locationName = null;
    private Dal dal = new Dal(this);
    protected int SOAP_TIME_OUT = 60000; // Used to specify SOAP Request Timeout

    //Declare variable for holding information through class
    String dayBefore;
    String monthBefore;
    String yearBefore;
    String dayAfter;
    String monthAfter;
    String yearAfter ;
    String dateBeforeSoap;
    String dateAfterSoap;
    String dateBeforeSQL;
    String dateAfterSQL;


    //Initilize
    Cursor cursor = null;
    SimpleCursorAdapter adapter = null;

    // Define meteorological widgets
    TextView airTemperatureTextView;
    TextView waterTemperatureTextView;
    TextView barometricPressureTextView;
    TextView rainFallTextView;
    TextView humidityTextView;
    TextView WindTextView;
    TextView visibilityTextView;
    TextView conductivityTextView;

    public String checkAddZero(String date)
    {
        if(date.length() == 1)
        {
            return date = "0"+date;
        }
        else
            return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Get information from previous activity
        Intent intent = getIntent();

        //Get location from activity 1 and location name
        locationSelection = intent.getExtras().getString("location");
        locationName = intent.getExtras().getString("location_name");

        //Used for testing purposes
        //Toast.makeText(this, locationSelection, Toast.LENGTH_LONG).show();

        //Get the first day (before date)
        dayBefore =  intent.getExtras().getString("dayBefore");
        monthBefore = intent.getExtras().getString("monthBefore");
        yearBefore = intent.getExtras().getString("yearBefore");

        //Get second date (after first date)
        dayAfter = intent.getExtras().getString("dayAfter");
        monthAfter = intent.getExtras().getString("monthAfter");
        yearAfter =intent.getExtras().getString("yearAfter");


        //Here I cahnge the date formation to add a 0 to the date, if needed
        dayBefore = checkAddZero(dayBefore);
        monthBefore = checkAddZero(monthBefore);
        dayAfter = checkAddZero(dayAfter);
        monthAfter = checkAddZero(monthAfter);


        //Here I have two different date formats I need
        //Date Format for Soap Request
         dateBeforeSoap =  yearBefore  + monthBefore + dayBefore;
         dateAfterSoap =  yearAfter + monthAfter  + dayAfter;

        //date Format for SQL
        dateBeforeSQL =  yearBefore + "/" + monthBefore+ "/" + dayBefore;
        dateAfterSQL =  yearAfter+ "/" + monthAfter+ "/"  + dayAfter;


        ///////////////////////////////////////////////////////////////////
        // Create a new asynchronous tasks for fetching data for display //
        ///////////////////////////////////////////////////////////////////

        //Datatide Task and launch it (execute) and pass in the location the user selected
        TideTask tideTask = new TideTask();
        tideTask.execute(locationSelection);

        // Set up the adapter for the ListView to display the forecast info
        adapter = new SimpleCursorAdapter(this,
                R.layout.listview_items,
                cursor,
                new String[]{"Time","HighLow", "Predictionsft"},
                new int[]{

                        R.id.timeTextView,
                        R.id.heightTextView,
                        R.id.tideHeight
                },
                0 );	// no flags

        //Gets ready for a toast, will display in feet and m the tide height
        ListView itemsListView = (ListView)findViewById(R.id.listView1);
        itemsListView.setAdapter(adapter);
        itemsListView.setOnItemClickListener(this);

        //Display locations name
        TextView locationNameDisplay = (TextView)findViewById(R.id.riverBayName);
        locationNameDisplay.setText(locationName);


        //Display locations name
        String time = DateFormat.getDateTimeInstance().format(new Date());

        TextView timeOnUpdate = (TextView)findViewById(R.id.lastUpdatedTime);
        timeOnUpdate.setText(time);


        // Initialize Display for Air Temperature
        airTemperatureTextView = (TextView)findViewById(R.id.air_temp_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        AirTemperatureTask air_temp_task = new AirTemperatureTask();
        air_temp_task.execute(locationSelection);

        // Initialize Display for Water Temperature
        barometricPressureTextView = (TextView)findViewById(R.id.bar_press_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        PressureTask pressureTask = new PressureTask();
        pressureTask.execute(locationSelection);

        // Initialize Display for Conductivity
        conductivityTextView = (TextView)findViewById(R.id.conductivity_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        ConductivityTask conductivityTask = new ConductivityTask();
        conductivityTask.execute(locationSelection);


        // Initialize Display for Water Temperature
        rainFallTextView= (TextView)findViewById(R.id.rain_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        RainFallTask rainFallTask = new RainFallTask();
        rainFallTask.execute(locationSelection);

        // Initialize Display for Water Temperature
        humidityTextView = (TextView)findViewById(R.id.humidity_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        HumidityTask humidityTask = new HumidityTask();
        humidityTask.execute(locationSelection);


        // Initialize Display for Water Temperature
        waterTemperatureTextView = (TextView)findViewById(R.id.water_temp_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        WaterTempTask water_temp_task = new WaterTempTask();
        water_temp_task.execute(locationSelection);


        // Initialize Display for WindTask
        WindTextView = (TextView)findViewById(R.id.wind_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        WindTask windTask = new WindTask();
        windTask.execute(locationSelection);


        // Initialize Display for Water Temperature
        visibilityTextView = (TextView)findViewById(R.id.visibility_value);

        //Datatide Task and launch it (execute) and pass in the location the user selected
        VisibilityTask visibilityTask = new VisibilityTask();
        visibilityTask.execute(locationSelection);



    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

     //Lets user click on the tide and tells you how high or low it will be
    public void onItemClick (AdapterView<?> parent, View view,int position, long id)
    {
        //Looks what position is
        cursor.moveToPosition(position);

        //Post message depending which one a person clicked on
        String myToast = cursor.getString(cursor.getColumnIndex("Predictionsft"))  + " ft " +  cursor.getString(cursor.getColumnIndex("Predictionscm")) + " m";
        Toast.makeText(this, myToast, Toast.LENGTH_SHORT).show();

    }
    /*** Background task to get a forecast from the web service ***/
    //Last string is return value
    private class TideTask extends AsyncTask<String, Void, String> {

        String XML_strings = "";

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {
        /////////////////////////////////////
        // SOAP Requests Run in Background //
        /////////////////////////////////////


                ///////////////////////
                // Tide SOAP Request //
                ///////////////////////

                // Create a SOAP request object and put it in an envelope
                final String TIDE_TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/highlowtidepred/wsdl";
                final String TIDE_OPERATION_NAME = "getHighLowTidePredictions";
                SoapObject request = new SoapObject(TIDE_TARGET_NAMESPACE, TIDE_OPERATION_NAME);

                //Request the information from the website
                request.addProperty("stationId", locationSelection);//florence
                request.addProperty("beginDate", dateBeforeSoap);
                request.addProperty("endDate", dateAfterSoap); //24 hour time
                request.addProperty("timeZone", 0);
                request.addProperty("unit", 1);

                //Have en envelope to take care of structure
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = false;
                envelope.setOutputSoapObject(request); //add request to envelope

                // Send the request (call the SOAP method)
                final String TIDE_ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred";

                HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, TIDE_ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
                //version tag infrastructure to give it
                final String SOAP_ACTION = TIDE_ENDPOINT + "/" + TIDE_OPERATION_NAME;

                ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

                //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
                ht.debug = true;

                //Try to get the information, catch various exceptions
                try {
                    //Call it, send request
                    ht.call(SOAP_ACTION, envelope);
                } catch (HttpResponseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Add Tide XML we got back
                //XML_strings = ;

                // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
                return  ht.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


                // reads it back from the db, check if there is an error
                if (result.contains("resource cannot be found")|| result.contains("No data was found")) {
                    //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
                } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                    // Process Tides XML File
                    dal.loadTideDbFromWebService(result, locationSelection);
                }


            //Use to test values
            //Toast.makeText(CoastalWeatherActivity.this,  dateBeforeSQL, Toast.LENGTH_LONG).show();

            //Update curserver , and call function to datebase to get forcast by location

            //This does the day requested and day after
            //cursor = dal.getForcastByLocation(locationSelection, dateBeforeSQL, dateAfterSQL);

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //This only does day requested
            cursor = dal.getForcastByLocation(locationSelection, dateBeforeSQL, dateBeforeSQL);

            //If the cursor didn't get a response, throw up error about no data
            if (cursor.getCount() == 0) {
                //Toast.makeText(CoastalWeatherActivity.this, "No saved entries", Toast.LENGTH_SHORT).show();
            } else { //Else update adapter and display
                adapter.changeCursor(cursor);
            }

            //Right here, if ther is no internet, we want to check if there is still anything in the data base
                //If there is display the old data
                //If there is nothing, say no saved entries and no internet

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Air Temperature SOAP Request                                       //
    //  http://opendap.co-ops.nos.noaa.gov/axis/webservices/airtemperature/wsdl/AirTemperature.wsdl //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class AirTemperatureTask extends AsyncTask<String, Void, String> {

        String XML_strings = null;

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {


                // Create a SOAP request object and put it in an envelope
                final String AP_TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/airtemperature/wsdl";
                final String AP_OPERATION_NAME = "getAirTemperature";
                SoapObject request_AP = new SoapObject(AP_TARGET_NAMESPACE, AP_OPERATION_NAME);

                //Request the information from the website
                request_AP.addProperty("stationId", locationSelection);// e.g. florence
                request_AP.addProperty("beginDate", dateBeforeSoap);
                request_AP.addProperty("endDate", dateAfterSoap); //24 hour time
                request_AP.addProperty("unit", "Fahrenheit"); // Choices are Celsius or Fahrenheit
                request_AP.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

                //Have en envelope to take care of structure
                SoapSerializationEnvelope envelope_AP = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope_AP.dotNet = false;
                envelope_AP.setOutputSoapObject(request_AP); //add request to envelope

                // Send the request (call the SOAP method)
                final String AP_ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/AirTemperature";

                HttpTransportSE ht_AP = new HttpTransportSE(Proxy.NO_PROXY, AP_ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
                //version tag infrastructure to give it
                final String SOAP_ACTION_AP = AP_ENDPOINT + "/" + AP_OPERATION_NAME;

                ht_AP.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

                //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
                ht_AP.debug = true;

                //Try to get the information, catch various exceptions
                try {
                    //Call it, send request
                    ht_AP.call(SOAP_ACTION_AP, envelope_AP);
                } catch (HttpResponseException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                }

                // Append the requested AirPressure XML to XML_strings
               // XML_strings = ;

                // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
                return ht_AP.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String airTemperature = "No service avialble at this time";
            // if(isOnline()) {
                // reads it back from the db, check if there is an error
                if (result.contains("resource cannot be found")|| result.contains("No data was found")){
                    //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
                } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                    // Process Tides XML File
                    dal.loadDbAirTempFromWebService(result, locationSelection);
                    airTemperature = dal.getAirTemperature(locationSelection, dateBeforeSQL, dateAfterSQL);
                    airTemperature = airTemperature + " \u2109";
                }
           // }
            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet
           // else {
           //     Toast.makeText(CoastalWeatherActivity.this, "No Internet", Toast.LENGTH_LONG).show();
           // }

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //Log.i("AirTemperatureTask>>", "Populating Widget");

            airTemperatureTextView.setText(airTemperature);

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Barometric Pressure SOAP Request                                           //
    //  http://opendap.co-ops.nos.noaa.gov/axis/webservices/barometricpressure/wsdl/BarometricPressure.wsdl //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class PressureTask extends AsyncTask<String, Void, String> {


        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {


            // Create a SOAP request object and put it in an envelope
            final String TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/barometricpressure/wsdl";
            final String OPERATION_NAME = "getBarometricPressure";
            SoapObject request = new SoapObject(TARGET_NAMESPACE, OPERATION_NAME);

            //Request the information from the website
            request.addProperty("stationId", locationSelection);// e.g. florence
            request.addProperty("beginDate", dateBeforeSoap);
            request.addProperty("endDate", dateAfterSoap); //24 hour time
            request.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request); //add request to envelope

            // Send the request (call the SOAP method)
            final String ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/BarometricPressure";

            HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION = ENDPOINT + "/" + OPERATION_NAME;

            ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht.call(SOAP_ACTION, envelope);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            //XML_strings =

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String pressureTemp = "No service available, at this time.";

            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found")|| result.contains("No data was found")) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
                dal.loadDbBarPressFromWebService(result, locationSelection);
                pressureTemp = dal.getBarometricPressure(locationSelection, dateBeforeSQL, dateAfterSQL);
                float temp = Float.valueOf(pressureTemp) * 0.401463078662F;
                String temp2 =  String.format("%.02f", temp);
                pressureTemp = temp2 + " inches";
            }

            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            barometricPressureTextView.setText(pressureTemp);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                          Conductivity SOAP Request                                          //
    // https://opendap.co-ops.nos.noaa.gov/axis/webservices/conductivity/wsdl/Conductivity.wsdl    //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class ConductivityTask extends AsyncTask<String, Void, String> {

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {

            // Create a SOAP request object and put it in an envelope
            final String AP_TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/conductivity/wsdl";
            final String AP_OPERATION_NAME = "getConductivity";
            SoapObject request_AP = new SoapObject(AP_TARGET_NAMESPACE, AP_OPERATION_NAME);

            //Request the information from the website
            request_AP.addProperty("stationId", locationSelection);// e.g. florence
            request_AP.addProperty("beginDate", dateBeforeSoap);
            request_AP.addProperty("endDate", dateAfterSoap); //24 hour time
            request_AP.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope_AP = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_AP.dotNet = false;
            envelope_AP.setOutputSoapObject(request_AP); //add request to envelope

            // Send the request (call the SOAP method)
            final String AP_ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/Conductivity";

            HttpTransportSE ht_AP = new HttpTransportSE(Proxy.NO_PROXY, AP_ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION_AP = AP_ENDPOINT + "/" + AP_OPERATION_NAME;

            ht_AP.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht_AP.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht_AP.call(SOAP_ACTION_AP, envelope_AP);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            // XML_strings = ;

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht_AP.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String conductivity = "No service available, at this time.";
            // if(isOnline()) {
            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found") || result.contains("No data was found")) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
               dal.loadDbConductivityFromWebService(result, locationSelection);
                conductivity = dal.getConductivity(locationSelection, dateBeforeSQL, dateAfterSQL);
            }
            // }
            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet
            // else {
            //     Toast.makeText(CoastalWeatherActivity.this, "No Internet", Toast.LENGTH_LONG).show();
            // }

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //Log.i("AirTemperatureTask>>", "Populating Widget");
            conductivityTextView.setText(conductivity );

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Rain SOAP Request                                       //
    //   //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class RainFallTask extends AsyncTask<String, Void, String> {

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {

            // Create a SOAP request object and put it in an envelope
            final String AP_TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/rainfall/wsdl";
            final String AP_OPERATION_NAME = "getRainFall";
            SoapObject request_AP = new SoapObject(AP_TARGET_NAMESPACE, AP_OPERATION_NAME);

            //Request the information from the website
            request_AP.addProperty("stationId", locationSelection);// e.g. florence
            request_AP.addProperty("beginDate", dateBeforeSoap);
            request_AP.addProperty("endDate", dateAfterSoap); //24 hour time
            request_AP.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope_AP = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_AP.dotNet = false;
            envelope_AP.setOutputSoapObject(request_AP); //add request to envelope

            // Send the request (call the SOAP method)
            final String AP_ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/RainFall";

            HttpTransportSE ht_AP = new HttpTransportSE(Proxy.NO_PROXY, AP_ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION_AP = AP_ENDPOINT + "/" + AP_OPERATION_NAME;

            ht_AP.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht_AP.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht_AP.call(SOAP_ACTION_AP, envelope_AP);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            // XML_strings = ;

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht_AP.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String rainFall = "No service available, at this time.";
            // if(isOnline()) {
            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found") || result.contains("No data was found") || result.contains("no data is available from this station") ) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
                dal.loadDbRainFallFromWebService(result, locationSelection);
                rainFall = dal.getRainFall(locationSelection, dateBeforeSQL, dateAfterSQL);
            }
            // }
            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet
            // else {
            //     Toast.makeText(CoastalWeatherActivity.this, "No Internet", Toast.LENGTH_LONG).show();
            // }

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //Log.i("AirTemperatureTask>>", "Populating Widget");
            rainFallTextView.setText(rainFall);

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Humidity SOAP Request                                              //
   //                                                                                               //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class HumidityTask extends AsyncTask<String, Void, String> {

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {

            // Create a SOAP request object and put it in an envelope
            final String AP_TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/relativehumidity/wsdl";
            final String AP_OPERATION_NAME = "getRelativeHumidity";
            SoapObject request_AP = new SoapObject(AP_TARGET_NAMESPACE, AP_OPERATION_NAME);

            //Request the information from the website
            request_AP.addProperty("stationId", locationSelection);// e.g. florence
            request_AP.addProperty("beginDate", dateBeforeSoap);
            request_AP.addProperty("endDate", dateAfterSoap); //24 hour time
            request_AP.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope_AP = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_AP.dotNet = false;
            envelope_AP.setOutputSoapObject(request_AP); //add request to envelope

            // Send the request (call the SOAP method)
            final String AP_ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/RelativeHumidity";

            HttpTransportSE ht_AP = new HttpTransportSE(Proxy.NO_PROXY, AP_ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION_AP = AP_ENDPOINT + "/" + AP_OPERATION_NAME;

            ht_AP.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht_AP.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht_AP.call(SOAP_ACTION_AP, envelope_AP);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            // XML_strings = ;

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht_AP.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String humidity = "No service available, at this time.";
            // if(isOnline()) {
            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found") || result.contains("No data was found") || result.contains("no data is available from this station") ) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
                dal.loadDbHumidityFromWebService(result, locationSelection);
                humidity = dal.getHumidity(locationSelection, dateBeforeSQL, dateAfterSQL);
            }
            // }
            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet
            // else {
            //     Toast.makeText(CoastalWeatherActivity.this, "No Internet", Toast.LENGTH_LONG).show();
            // }

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //Log.i("AirTemperatureTask>>", "Populating Widget");
            humidityTextView.setText(humidity);

        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Water Temperature SOAP Request                                           //
    //  http://opendap.co-ops.nos.noaa.gov/axis/webservices/watertemperature/wsdl/WaterTemperature.wsdl   //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class WaterTempTask extends AsyncTask<String, Void, String> {

        String XML_strings = null;

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {


                // Create a SOAP request object and put it in an envelope
                final String TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/watertemperature/wsdl";
                final String OPERATION_NAME = "getWaterTemperature";
                SoapObject request = new SoapObject(TARGET_NAMESPACE, OPERATION_NAME);

                //Request the information from the website
                request.addProperty("stationId", locationSelection);// e.g. florence
                request.addProperty("beginDate", dateBeforeSoap);
                request.addProperty("endDate", dateAfterSoap); //24 hour time
                request.addProperty("unit", "Fahrenheit"); // Choices are Celsius or Fahrenheit
                request.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

                //Have en envelope to take care of structure
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = false;
                envelope.setOutputSoapObject(request); //add request to envelope

                // Send the request (call the SOAP method)
                final String ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/WaterTemperature";

                HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
                //version tag infrastructure to give it
                final String SOAP_ACTION = ENDPOINT + "/" + OPERATION_NAME;

                ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

                //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
                ht.debug = true;

                //Try to get the information, catch various exceptions
                try {
                    //Call it, send request
                    ht.call(SOAP_ACTION, envelope);
                } catch (HttpResponseException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    Log.e("ht_AP >> ", e.toString());
                    e.printStackTrace();
                }

                // Append the requested AirPressure XML to XML_strings
                //XML_strings =

                // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
                return ht.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String waterTemp = "No service aviablibe at this time";
                // reads it back from the db, check if there is an error
                if (result.contains("resource cannot be found")|| result.contains("No data was found")) {
                    //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
                } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                    // Process Tides XML File
                    dal.loadDbWaterTempFromWebService(result, locationSelection);
                     waterTemp = dal.getWaterTemperature(locationSelection, dateBeforeSQL, dateAfterSQL);
                     waterTemp = waterTemp + " \u2109";
                }

            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////
            waterTemperatureTextView.setText(waterTemp);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Wind SOAP Request                                       //
    //   //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class WindTask extends AsyncTask<String, Void, String> {


        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {


            // Create a SOAP request object and put it in an envelope
            final String TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/wind/wsdl";
            final String OPERATION_NAME = "getWind";
            SoapObject request = new SoapObject(TARGET_NAMESPACE, OPERATION_NAME);

            //Request the information from the website
            request.addProperty("stationId", locationSelection);// e.g. florence
            request.addProperty("beginDate", dateBeforeSoap);
            request.addProperty("endDate", dateAfterSoap); //24 hour time
            request.addProperty("unit", "Miles"); // Choices are Celsius or Fahrenheit
            request.addProperty("timeZone", 1); // Choices are 0 for GMT, 1 for Local Time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request); //add request to envelope

            // Send the request (call the SOAP method)
            final String ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/Wind";

            HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION = ENDPOINT + "/" + OPERATION_NAME;

            ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht.call(SOAP_ACTION, envelope);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            //XML_strings =

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String windTemp = "No service aviablibe at this time";
            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found")|| result.contains("No data was found")) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
                dal.loadDbWindFromWebService(result, locationSelection);
                windTemp = dal.getWind(locationSelection, dateBeforeSQL, dateAfterSQL);
                String[] split = windTemp.split(",");
                String part1 = split[0]; // Speed
                String part2 = split[1]; // Direction
                String part3 = split[2]; //Gust
                windTemp = part1 + " mph \n" + part2+ "(degree)\n" + part3 + "mph gusts";
            }

            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////
            WindTextView.setText(windTemp);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Visibility SOAP Request                                       //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////


    private class VisibilityTask extends AsyncTask<String, Void, String> {

        String XML_strings = null;

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {


            // Create a SOAP request object and put it in an envelope
            final String TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/visibility/wsdl";
            final String OPERATION_NAME = "getVisibility";
            SoapObject request = new SoapObject(TARGET_NAMESPACE, OPERATION_NAME);

            //Request the information from the website
            request.addProperty("stationId", locationSelection);// e.g. florence
            request.addProperty("beginDate", dateBeforeSoap);
            request.addProperty("endDate", dateAfterSoap); //24 hour time

            //Have en envelope to take care of structure
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request); //add request to envelope

            // Send the request (call the SOAP method)
            final String ENDPOINT = "https://opendap.co-ops.nos.noaa.gov/axis/services/Visibility";

            HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, ENDPOINT, SOAP_TIME_OUT); //60000 60 second to time out if no response
            //version tag infrastructure to give it
            final String SOAP_ACTION = ENDPOINT + "/" + OPERATION_NAME;

            ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            //True sends us back raw xml for the response (eles it would try to parse for us, but we have a parser)
            ht.debug = true;

            //Try to get the information, catch various exceptions
            try {
                //Call it, send request
                ht.call(SOAP_ACTION, envelope);
            } catch (HttpResponseException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.e("ht_AP >> ", e.toString());
                e.printStackTrace();
            }

            // Append the requested AirPressure XML to XML_strings
            //XML_strings =

            // Finally Return all XML files as a big string with each file delimited by the XML_delimiter
            return ht.responseDump;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String visibility = "No service aviablibe at this time";
            // reads it back from the db, check if there is an error
            if (result.contains("resource cannot be found")|| result.contains("No data was found")) {
                //Toast.makeText(CoastalWeatherActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                // Process Tides XML File
                dal.loadDbVisibilityFromWebService(result, locationSelection);
                visibility = dal.getVisibility(locationSelection, dateBeforeSQL, dateAfterSQL);

            }

            //Right here, if ther is no internet, we want to check if there is still anything in the data base
            //If there is display the old data
            //If there is nothing, say no saved entries and no internet

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////
            visibilityTextView.setText(visibility);
        }

    }

}