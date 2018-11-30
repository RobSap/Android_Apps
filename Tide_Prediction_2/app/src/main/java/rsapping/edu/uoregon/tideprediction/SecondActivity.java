package rsapping.edu.uoregon.tideprediction;


import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
          {

    private TextView textView;
    private TideItems tideItems;

    String locationSelection = "Florence";
    private Dal dal = new Dal(this);

    Cursor cursor = null;
    SimpleCursorAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Get infromation from previous activity
        Intent intent = getIntent();
        String location = intent.getExtras().getString("location");

        String date1 = intent.getExtras().getString("day");
        String date2 = intent.getExtras().getString("month");

        //Here change date formation to required type, so add 0's if needed
        if(date1.length() == 1)
            date1 = "0"+date1;

        if(date2.length() == 1)
            date2 = "0"+date2;


        //They can only pick 2016, so I didn't need to pass it anyways
        String date =  "2016/" + date2 + "/" + date1;

        // Get Forecast for the default location
        cursor = dal.getForcastByLocation(location,date);


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



        //Gets ready for a toast
        ListView itemsListView = (ListView)findViewById(R.id.listView1);
        itemsListView.setAdapter(adapter);
       itemsListView.setOnItemClickListener(this);

    }

     //Lets user click on the tide and tells you how high or low it will be
    public void onItemClick (AdapterView<?> parent, View view,int position, long id)
    {
    cursor.moveToPosition(position);
    String myToast = cursor.getString(cursor.getColumnIndex("Predictionsft"))  + " ft " +  cursor.getString(cursor.getColumnIndex("Predictionscm")) + " cm";
    Toast.makeText(this, myToast, Toast.LENGTH_LONG).show();

    }


}