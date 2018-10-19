package rsapping.edu.uoregon.tideprediction;

import java.io.InputStream;
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



// Data Access Layer
public class Dal {
    private Context context = null;

    public Dal(Context context) {
        this.context = context;
    }


    // This is a temporary method for loading fixed data into the db
    public void loadTestData(String city)
    {
        // Initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // load the database with test data if it isn't already loaded
        if (db.rawQuery("SELECT * FROM Tides WHERE City = '" + city + "'", null).getCount() == 0)
        {
            //Load only the city the person selects, it will load faster this way
            loadDbFromXML(city);	// Eugene

        }
        db.close();
    }

    // Parse the XML files and put the data in the db
    public void loadDbFromXML( String city) {

        String fileName = city +".xml";
        TideItems items = parseXml(fileName);

        items.setCity(city);	// This field isn't in the xml file, so we add it here


        // Initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // Put weather forecast in the database
        ContentValues cv = new ContentValues();

        for (TideItem item :items) {
            cv.put("Date", item.getDate());
            cv.put("City", items.getCity());
            cv.put("Day", item.getDay());
            cv.put("Predictionsft", item.getPredictionsft());
            cv.put("Predictionscm", item.getPredictionscm());
            cv.put("Highlow", item.getHighLow());
            cv.put("Time", item.getTime());
            db.insert("Tides", null, cv);

        }
        db.close();
    }

    //Get the forcase by location (location only)
    public Cursor getForcastByLocation(String location, String date) {

        loadTestData(location);

        // Initialize the database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Get a tide forecast for one location , with the dates chosen
        String query = "SELECT * FROM Tides WHERE City = ? AND Date = ? ORDER BY Date ASC";
        String[] variables = new String[]{location,date};    // rawQuery must not include a trailing ';'

        //Return the request I put in
        return db.rawQuery(query, variables);
    }


    //This parses the files from assests
    public TideItems parseXml(String fileName) {
        try {

            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            ParseHandler tideParseHandler = new ParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(tideParseHandler);

            // read the file from internal storage
            InputStream in = context.getAssets().open(fileName);

            // parse the data
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            // return a forecast object which contains a list of parsed weather data
            TideItems tides = tideParseHandler.getItems();
            return tides;

        }
        catch (Exception e) {
            Log.e("News reader", e.toString());
            return null;
        }
    }


}