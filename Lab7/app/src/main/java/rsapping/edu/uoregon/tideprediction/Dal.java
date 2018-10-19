package rsapping.edu.uoregon.tideprediction;

import java.io.InputStream;
import java.io.StringReader;
//import java.io.StringReader;
import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


// Data Access Layer
public class Dal {
    private Context context = null;

    public Dal(Context context) {
        this.context = context;
    }


    //this function is to make sure the entry we are about to insert isn't in the DB already
    //I pass in the current DB to check, and the location, the specific date and time
    public boolean checkDuplicate( SQLiteDatabase db ,String location, String date, String time)
    {
       String query = "SELECT * FROM Tides WHERE City = ? AND Date = ? AND Time = ?";
        String[] variables = new String[]{location, date, time};    // rawQuery must not include a trailing ';'

        // load the database with test data if it isn't already loaded
        if (db.rawQuery(query, variables).getCount() == 0)
        {
            //if its empty return false
            return false;

        }
        //if its in there return true
        return true;
    }

    // Parse the XML files and put the data in the db
    public void loadDbFromWebService(String xmlData, String city) {


        //Parse the items, and pass results into tide items
        TideItems items = parseXml(xmlData);

        //add the city code
        items.setCity(city);	// This field isn't in the xml file, so we add it here


        // Initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // Put weather forecast in the database
        ContentValues cv = new ContentValues();

        //Toast.makeText(SecondActivity.this,  dateAfter, Toast.LENGTH_LONG).show();
        for (TideItem item :items) {

            //This function askes the question, is this entry about to submit in there?
            if(checkDuplicate(db,items.getCity(), item.getDate(), item.getTime())  ==false)  {
                cv.put("Date", item.getDate());
                cv.put("City", items.getCity());
                cv.put("Day", item.getDay());
                cv.put("Predictionsft", item.getPredictionsft());
                cv.put("Predictionscm", item.getPredictionscm());
                cv.put("Highlow", item.getHighLow());
                cv.put("Time", item.getTime());
                db.insert("Tides", null, cv);
            }

        }
        db.close();
    }


    //Get the the forcast by location and by the 2 dates
    public Cursor getForcastByLocation(String location, String dateBefore, String dateAfter ) {

        // Initialize the database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Get a tide forecast for one location , with the dates chosen
        //String query = "SELECT * FROM Tides WHERE City = ? AND Date(Date) BETWEEEN ? AND ? ORDER BY Date ASC";
        String query = "SELECT * FROM Tides WHERE City = ? AND (Date = ? OR Date = ?) ORDER BY Date ASC";
        String[] variables = new String[]{location, dateBefore,dateAfter};    // rawQuery must not include a trailing ';'

        //Return the request I put in
        return db.rawQuery(query, variables);
    }


    //This parses the files from assests
    public TideItems parseXml(String xmlData) {
        try {

            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            ParseHandler tideParseHandler = new ParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(tideParseHandler);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            TideItems tides = tideParseHandler.getItems();
            return tides;

        }
        catch (Exception e) {
            Log.e("Tide reader", e.toString());
            return null;
        }
    }


}