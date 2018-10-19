package rsapping.edu.uoregon.tideprediction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private FileIO io;
    private TideItems tideItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up file, read in
        io = new FileIO(getApplicationContext());
        tideItems = io.readFile();


        //Create array list to store stuff (hash map of items)
        final ArrayList<HashMap<String, String>>  data = new
                ArrayList<HashMap<String, String>>();


        //Pull info, add to hashmap for specific item
        for (TideItem item : tideItems.getItems())
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", item.getDate() + " " +item.getDay() );
            map.put("predictions", item.getPredictions_in_ft() + " ft " + item.getPredictions_cm() + " cm") ;
            map.put("highLow", item.getHighLow() + " " + item.getTime());
            data.add(map);
        }

        //Set the adapter to display Text
        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                R.layout.listview_items,
                new String[]{"date","highLow" ,},
                new int[]{R.id.dateTextView, R.id.tideTextView}

        );
        final ListView itemsListView = (ListView)findViewById(R.id.listView1);
        itemsListView.setAdapter(adapter);

        //Toast to show prediction of ft or cm of tide size
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v, int arg2,long arg3) {
                //here v is your Tite Item's layout.
                Toast.makeText(getApplicationContext(), "Height of tide " + data.get(arg2).get("predictions"), Toast.LENGTH_SHORT).show();
            }
        });

    }




}
