package rsapping.edu.uoregon.tideprediction;


import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class TidesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
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

    TextView airTemperatureTextView;

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
        setContentView(R.layout.activity_tides);

        //Get information from previous activity
        Intent intent = getIntent();

        //Get location
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

        //Used for testing purposes
      //  Toast.makeText(this, dayBefore, Toast.LENGTH_LONG).show();

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

        //Create a new tide Task and launch it (execute) and pass in the location the user selected
        TideTask tideTask = new TideTask();
        tideTask.execute(locationSelection);

        // Set up the adapter for the ListView to display the forecast info
        adapter = new SimpleCursorAdapter(this,
                R.layout.listview_items2,
                cursor,
                new String[]{"Date","Day","Time","HighLow"},
                new int[]{

                        R.id.dateTextView,
                        R.id.dayTextView,
                        R.id.timeTextView,
                        R.id.heightTextView
                },
                0 );	// no flags

        //Gets ready for a toast, will display in feet and m the tide height
        ListView itemsListView2 = (ListView)findViewById(R.id.listView1);
        itemsListView2.setAdapter(adapter);
        itemsListView2.setOnItemClickListener(this);


        //Display locations name
        String time = DateFormat.getDateTimeInstance().format(new Date());
        TextView timeOnUpdate = (TextView)findViewById(R.id.lastUpdatedTime);
        timeOnUpdate.setText(time);


        //Display locations name
        TextView locationNameDisplay = (TextView)findViewById(R.id.riverBayName);
        locationNameDisplay.setText(locationName);

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

        //String XML_strings = "";

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {
            /////////////////////////////////////
            // SOAP Requests Run in Background //
            /////////////////////////////////////

           // if(isOnline()) {
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

                // Joey !!!
                // broke my xml strings crap here... it requires delimter
                // Not sure what you did to my dal but I cant just return XML string. I HAVE
                // to leave all this or it won't work. This looks terrible !!
                //XML_strings = XML_string_delimeter + ht.responseDump;
               /// XML_strings = XML_strings + XML_string_delimeter;
               /// XML_strings = XML_strings + XML_string_delimeter;
               /// XML_strings = XML_strings + XML_string_delimeter;
                //return XML_strings;



            //Below is commented out to add the isonline function back. It does not work on the pc but works on a phone.
                 // Finally Return all XML files as a big string with each file delimited by the XML_delimiter


                return  ht.responseDump;

                //  }

                 //Whatever returns from here, will be in the result in onPostExecute
                 //  return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Online is only commented out for testing on pc, it will work on cells and be added back later
          //  if(isOnline()) {
                // reads it back from the db, check if there is an error
                if (result.contains("resource cannot be found")) {
                    Toast.makeText(TidesActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
                } else { //If no error pass the xml results into the loadDBfromWebSerice Function
                    // Array containing all of the XMLs from SOAP Requests as concatenated strings


                    //Joey !!!
                    // //remove this stuff !!! it should just be able to call loadDBFromWEbService.
                    // loadTideDbFromWebService can hold the only non tide data if you want... (since they can all come at once)
                    //String[] splitXMLS = result.split(XML_string_delimeter);

                    //System.out.println("result = " + result);
                    //System.out.println("splitXMLs[0] = " + splitXMLS[0]);  splitXMLS[0] is empty
                    //System.out.println("splitXMLs[1] = " + splitXMLS[1]);
                    //System.out.println("splitXMLs[2] = " + splitXMLS[2]);

                    // Process Tides XML File
                    //Joey !!
                    //For just tides this should be  dal.loadTideDbFromWebService(result, locationSelection);
                    dal.loadTideDbFromWebService(result, locationSelection);

                }

          //  }
          //  else {
          //      Toast.makeText(TidesActivity.this, "No Internet", Toast.LENGTH_LONG).show();
          //  }

            //Use to test values
            //Toast.makeText(SecondActivity.this,  dateBeforeSQL, Toast.LENGTH_LONG).show();

            //Update curserver , and call function to datebase to get forcast by location

            //This does the day requested and day after
            //cursor = dal.getForcastByLocation(locationSelection, dateBeforeSQL, dateAfterSQL);

            //////////////////////////////
            // Populate Display Widgets //
            //////////////////////////////

            //This only does day requested
            cursor = dal.getForcastByLocation(locationSelection, dateBeforeSQL, dateAfterSQL);

            //If the cursor didn't get a response, throw up error about no data
            if (cursor.getCount() == 0) {
                Toast.makeText(TidesActivity.this, "No saved entries", Toast.LENGTH_SHORT).show();
            } else { //Else update adapter and display
                adapter.changeCursor(cursor);
            }

        }

    }

}