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
import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;



public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{



    String locationSelection = "";
    String date = "2016/01/01";
    private Dal dal = new Dal(this);

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

        //Get location
        locationSelection = intent.getExtras().getString("location");

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

        /* Legacy code
        //Here change date formation to required type, so add 0's if needed
        if(dayBefore.length() == 1)
            dayBefore = "0"+dayBefore;

        if(monthBefore.length() == 1)
            monthBefore = "0"+monthBefore;


        //Here change date formation to required type, so add 0's if needed
        if(dayAfter.length() == 1)
            dayAfter = "0"+dayAfter;

        //twoDayForcast = twoDayForcast + 1;

        if(monthAfter.length() == 1)
            monthAfter = "0"+monthAfter;
        */

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
                R.layout.listview_items,
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
        ListView itemsListView = (ListView)findViewById(R.id.listView1);
        itemsListView.setAdapter(adapter);
        itemsListView.setOnItemClickListener(this);

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

        //Do in background launches a new thread to do the task so user sees no lag or load issues
        @Override
        protected String doInBackground(String... params) {

            // Create a SOAP request object and put it in an envelope
            final String TARGET_NAMESPACE = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/highlowtidepred/wsdl";
            final String OPERATION_NAME = "getHighLowTidePredictions";
            SoapObject request = new SoapObject(TARGET_NAMESPACE, OPERATION_NAME);


            //Request the infromation from the website
            request.addProperty("stationId", locationSelection);//florence
            request.addProperty("beginDate",dateBeforeSoap);
            request.addProperty("endDate", dateAfterSoap); //24 hour time
            request.addProperty("timeZone", 0);
            request.addProperty("unit", 1);

            //Have en envlope to take care of structure
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request); //add request to envelope

            // Send the request (call the SOAP method)
             final String ENDPOINT =  "https://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred";

             HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, ENDPOINT , 60000); //60000 60 second to time out if no response
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
             // TODO Auto-generated catch block
             e.printStackTrace();
             } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             } catch (XmlPullParserException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             }

            //take the xml I got back, and return it
             String tideXml = ht.responseDump;
             return tideXml;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // reads it back from the db, check if there is an error
            if(result.contains("resource cannot be found")){
                Toast.makeText(SecondActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            }
            else
            { //If no error pass the xml results into the loadDBfromWebSerice Function
                dal.loadDbFromWebService(result, locationSelection);
            }
            //Use to test values
                //Toast.makeText(SecondActivity.this,  dateBeforeSQL, Toast.LENGTH_LONG).show();

            //Update curserver , and call function to datebase to get forcast by location
            cursor = dal.getForcastByLocation(locationSelection, dateBeforeSQL, dateAfterSQL);

            //If the cursor didn't get a response, throw up error about no data
            if(cursor.getCount() == 0)
            {
                Toast.makeText(SecondActivity.this, "No data available", Toast.LENGTH_SHORT).show();
            }
            else
            { //Else update adapter and display
                adapter.changeCursor(cursor);
            }
        }

    }


}