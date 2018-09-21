package rsapping.edu.uoregon.tideprediction;

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


// Data Access Layer
public class Dal {
    private Context context = null;

    public Dal(Context context) {
        this.context = context;
    }


    //this function is to make sure the entry we are about to insert isn't in the DB already
    //I pass in the current DB to check, and the location, the specific date and time
    public boolean checkDuplicate(SQLiteDatabase db, String table, String location, String date, String time) { // date == DateTime for meteorological data
        if (table == null) {
            Log.e("checkDuplicate()", "table is NULL)");
        }
        if (date == null) {
            Log.e("checkDuplicate()", "date is NULL)");
        }
        if (location == null) {
            Log.e("checkDuplicate()", "location is NULL)");
        }
        if (time == null) {
            Log.e("checkDuplicate()", "time is NULL)");
        }

        String query = null;
        String[] variables = null;
        // Prepare Query for Tides Table
        if (table == "Tides") {
            query = "SELECT * FROM Tides WHERE City = ? AND Date = ? AND Time = ?";
            variables = new String[]{location, date, time};    // rawQuery must not include a trailing ';'
        }
        // Prepare Query for Meteorological Tables

        else if (table == "AirTemp" || table == "WaterTemp" || table == "BarPress" || table == "Wind" || table == "RainFall" || table == "Visibility" || table == "Humidity") { // Assume Query is for Meteorological data
            String dateTime = date + " " + time;
            query = "SELECT * FROM " + table + " WHERE City = ? AND DateTime = ?";
            variables = new String[]{location, dateTime};    // rawQuery must not include a trailing ';'
        }

        // Test for Duplicate Entry
        if (db.rawQuery(query, variables).getCount() == 0) {
            //if its empty return false
            return false;
        }

        return true;
    }

    ///////////////////////////////////////
    ////                               ////
    //        1. Tide Predictions        //
    ////                               ////
    ///////////////////////////////////////


    /**
     * This function reads, parses, and stores the tide prediction data from the soap request
     *
     * @param xmlData XML file returned by SOAP Request
     * @param city    Location of request
     */
    public void loadTideDbFromWebService(String xmlData, String city) {

        Log.i("loadTideDbFromWeb...()", xmlData);

        //Parse the items, and pass results into tide items
        TideItems items = parseXml(xmlData);

        //add the city code
        items.setCity(city);    // This field isn't in the xml file, so we add it here

        // Initialize database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // Put weather forecast in the database
        ContentValues cv = new ContentValues();

        //Toast.makeText(CoastalWeatherActivity.this,  dateAfter, Toast.LENGTH_LONG).show();
        for (TideItem item : items) {

            //This function askes the question, is this entry about to submit in there?
            if (checkDuplicate(db, "Tides", items.getCity(), item.getDate(), item.getTime()) == false) {
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

        // Finally Close the Database
        db.close();

    }

    //This parses the files from assests
    public TideItems parseXml(String xmlData) {
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            TideParseHandler tideParseHandler = new TideParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(tideParseHandler);

            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            TideItems tides = tideParseHandler.getItems();
            return tides;

        } catch (Exception e) {
            Log.e("Tide reader", e.toString());
            return null;
        }
    }

    //Get the the forcast by location and by the 2 dates
    public Cursor getForcastByLocation(String location, String dateBefore, String dateAfter) {

        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Get a tide forecast for one location , with the dates chosen
        //String query = "SELECT * FROM Tides WHERE City = ? AND Date(Date) BETWEEEN ? AND ? ORDER BY Date ASC";
        String query = "SELECT * FROM Tides WHERE City = ? AND (Date = ? OR Date = ?) ORDER BY Date ASC";
        String[] variables = new String[]{location, dateBefore, dateAfter};    // rawQuery must not include a trailing ';'

        //Return the request I put in
        return db.rawQuery(query, variables);
    }

    //////////////////////////////////////
    ////                              ////
    //        Air Temperature        //
    ////                              ////
    //////////////////////////////////////


    /**
     * This function reads, parses, and stores the air temperature data from the soap request
     *
     * @param xmlData XML file returned by SOAP Request
     * @param city    Location of request
     */
    public void loadDbAirTempFromWebService(String xmlData, String city) {

        Log.i("loadDbAirTemp...", xmlData);

        //Parse the items, and pass results into tide items
        MeteorologicalItems items2 = parseXmlAirTemps(xmlData);

        // Error checking for fault codes
        if (items2.getFaultCode() != "none") { // Log Error
            Log.e("AirTemperature->SOAP", "FaultCode = " + items2.getFaultCode());
        }
        // Error checking for > 0 items
        if (items2.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items2.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();


            // Put weather forecast in the database
            ContentValues cv_airTemps = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem2 = items2.get(items2.size() - 1); // last item

            Log.e("DateTime:", (currentItem2.getDate() + " " + currentItem2.getTime()));

            if (checkDuplicate(db_read, "AirTemp", items2.getCity(), currentItem2.getDate(), currentItem2.getTime()) == false) {
                Log.e("AirTemperature", "checkDuplicate()==false");
                cv_airTemps.put("City", items2.getCity());
                cv_airTemps.put("DateTime", currentItem2.getDate() + " " + currentItem2.getTime());
                cv_airTemps.put("Day", currentItem2.getDay());
                cv_airTemps.put("Value", currentItem2.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("AirTemp", null, cv_airTemps); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    public MeteorologicalItems parseXmlAirTemps(String xmlData) {
        //Log.i("parseXmlAirTemps()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            AirTempParseHandler airTempPH = new AirTempParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(airTempPH);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems airTemps = airTempPH.getItems();
            //Log.i("parseXmlAirTemps()", "Finished");
            return airTemps;
        } catch (Exception e) {
            Log.e("parseXmlAirTemps", e.toString());
            return null;
        }

    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent air temperature based on specified parameters
     */
    public String getAirTemperature(String location, String dateBefore, String dateAfter) {
        Log.i("getAirTemperature()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM AirTemp WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        //String query="SELECT * FROM AirTemp WHERE City = ? ORDER BY DateTime DESC";
        //String[] variables = new String[]{location, dateBefore, dateAfter};    // rawQuery must not include a trailing ';'
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            //Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        //Log.i("getAirTemperature()", "Result = " + result);
        return result;
    }



    ///////////////////////////////////////
    ////                               ////
    //   Barometric Pressure             //
    ////                               ////
    ///////////////////////////////////////

    public void loadDbBarPressFromWebService(String xmlData, String city) {

        Log.i("loadDbPressure...", xmlData);

        //Parse the items
        MeteorologicalItems items4 = parseXmlBarPress(xmlData);

        // Error checking for fault codes
        if (items4.getFaultCode() != "none") { // Log Error
            Log.e("Bar Press->SOAP", "FaultCode = " + items4.getFaultCode());
        }
        // Error checking for > 0 items
        if (items4.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items4.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem4 = items4.get(items4.size() - 1); // last item

            if (checkDuplicate(db_read, "BarPress", items4.getCity(), currentItem4.getDate(), currentItem4.getTime()) == false) {
                Log.e("BarPress", "checkDuplicate()==false");
                cv.put("City", items4.getCity());
                cv.put("DateTime", currentItem4.getDate() + " " + currentItem4.getTime());
                cv.put("Day", currentItem4.getDay());
                cv.put("Value", currentItem4.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("BarPress", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent bar pressure based on specified parameters
     */
    public String getBarometricPressure(String location, String dateBefore, String dateAfter) {
        Log.i("getBarometricPressure()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM BarPress WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        //String query="SELECT * FROM AirTemp WHERE City = ? ORDER BY DateTime DESC";
        //String[] variables = new String[]{location, dateBefore, dateAfter};    // rawQuery must not include a trailing ';'
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        Log.i("getBarometricPressure()", "Result = " + result);
        return result;
    }


    public MeteorologicalItems parseXmlBarPress(String xmlData) {
        //Log.i("parseXmlAirTemps()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            BarometricPressureParseHandler ph = new BarometricPressureParseHandler();
            Log.e("parseXmlBarPress", " </set content handler>");
            //Its making me cast this as a content handler
            xmlreader.setContentHandler(ph);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems barPress = ph.getItems();
            Log.e("parseXmlBarPress", "LENGTH OF barPress = " + barPress);
            //Log.i("parseXmlAirTemps()", "Finished");

            return barPress;

        } catch (Exception e) {
            Log.e("parseXmlBarPress EXCPT", e.toString());
            return null;
        }

    }

    //////////////////////////////////////
    ////                              ////
    //        Conductivity              //
    ////                              ////
    //////////////////////////////////////

    public void loadDbConductivityFromWebService(String xmlData, String city) {

        Log.i("loadDbPressure...", xmlData);

        //Parse the items
        MeteorologicalItems items5 = parseXmlConPress(xmlData);

        // Error checking for fault codes
        if (items5.getFaultCode() != "none") { // Log Error
            Log.e("Bar Press->SOAP", "FaultCode = " + items5.getFaultCode());
        }
        // Error checking for > 0 items
        if (items5.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items5.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem5 = items5.get(items5.size() - 1); // last item

            if (checkDuplicate(db_read, "Con", items5.getCity(), currentItem5.getDate(), currentItem5.getTime()) == false) {
                Log.e("Conductivity", "checkDuplicate()==false");
                cv.put("City", items5.getCity());
                cv.put("DateTime", currentItem5.getDate() + " " + currentItem5.getTime());
                cv.put("Day", currentItem5.getDay());
                cv.put("Value", currentItem5.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("Con", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent bar pressure based on specified parameters
     */
    public String getConductivity(String location, String dateBefore, String dateAfter) {
        //Log.i("getConductivity()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM Con WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        //Log.i("getCon()", "Result = " + result);
        return result;
    }


    public MeteorologicalItems parseXmlConPress(String xmlData) {
        //Log.i("parseCon()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            ConductivityParseHandler ph = new ConductivityParseHandler();
            //Log.e("parseXmlCon", " </set content handler>");
            //Its making me cast this as a content handler
            xmlreader.setContentHandler(ph);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems barPress = ph.getItems();
            //Log.e("parseXmlBarPress", "LENGTH OF barPress = " + barPress);
            //Log.i("parseXmlAirTemps()", "Finished");

            return barPress;

        } catch (Exception e) {
           // Log.e("parseXmlConductivity EXCPT", e.toString());
            return null;
        }

    }
    //////////////////////////////////////
    ////                              ////
    //        Rain                      //
    ////                              ////
    //////////////////////////////////////

    public void loadDbRainFallFromWebService(String xmlData, String city) {

        Log.i("loadDbPressure...", xmlData);

        //Parse the items
        MeteorologicalItems items6 = parseXmlRainPress(xmlData);

        // Error checking for fault codes
        if (items6.getFaultCode() != "none") { // Log Error
            Log.e("Rain Fall->SOAP", "FaultCode = " + items6.getFaultCode());
        }
        // Error checking for > 0 items
        if (items6.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items6.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem5 = items6.get(items6.size() - 1); // last item

            if (checkDuplicate(db_read, "RainFall", items6.getCity(), currentItem5.getDate(), currentItem5.getTime()) == false) {
                Log.e("RainFall", "checkDuplicate()==false");
                cv.put("City", items6.getCity());
                cv.put("DateTime", currentItem5.getDate() + " " + currentItem5.getTime());
                cv.put("Day", currentItem5.getDay());
                cv.put("Value", currentItem5.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("RainFall", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent bar pressure based on specified parameters
     */
    public String getRainFall(String location, String dateBefore, String dateAfter) {
        //Log.i("getConductivity()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM RainFall WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        //Log.i("getCon()", "Result = " + result);
        return result;
    }


    public MeteorologicalItems parseXmlRainPress(String xmlData) {
        //Log.i("parseCon()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            RainFallParseHandler ph = new RainFallParseHandler();
            //Log.e("parseXmlCon", " </set content handler>");
            //Its making me cast this as a content handler
            xmlreader.setContentHandler(ph);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems rain = ph.getItems();
            //Log.e("parseXmlBarPress", "LENGTH OF barPress = " + barPress);
            //Log.i("parseXmlAirTemps()", "Finished");

            return rain;

        } catch (Exception e) {
            // Log.e("parseXmlConductivity EXCPT", e.toString());
            return null;
        }

    }
    //////////////////////////////////////
    ////                              ////
    //        Humidity                  //
    ////                              ////
    //////////////////////////////////////

    public void loadDbHumidityFromWebService(String xmlData, String city) {

        Log.i("loadDbPressure...", xmlData);

        //Parse the items
        MeteorologicalItems items6 = parseXmlHumidity(xmlData);

        // Error checking for fault codes
        if (items6.getFaultCode() != "none") { // Log Error
            Log.e("Rain Fall->SOAP", "FaultCode = " + items6.getFaultCode());
        }
        // Error checking for > 0 items
        if (items6.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items6.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem5 = items6.get(items6.size() - 1); // last item

            if (checkDuplicate(db_read, "Humidity", items6.getCity(), currentItem5.getDate(), currentItem5.getTime()) == false) {
                Log.e("RainFall", "checkDuplicate()==false");
                cv.put("City", items6.getCity());
                cv.put("DateTime", currentItem5.getDate() + " " + currentItem5.getTime());
                cv.put("Day", currentItem5.getDay());
                cv.put("Value", currentItem5.getPrimaryValue() );

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("Humidity", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent bar pressure based on specified parameters
     */
    public String getHumidity(String location, String dateBefore, String dateAfter) {
        //Log.i("getConductivity()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM Humidity WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        //Log.i("getCon()", "Result = " + result);
        return result;
    }


    public MeteorologicalItems parseXmlHumidity(String xmlData) {
        //Log.i("parseCon()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            HumidityParseHandler ph = new HumidityParseHandler();
            //Log.e("parseXmlCon", " </set content handler>");
            //Its making me cast this as a content handler
            xmlreader.setContentHandler(ph);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems rain = ph.getItems();
            //Log.e("parseXmlBarPress", "LENGTH OF barPress = " + barPress);
            //Log.i("parseXmlAirTemps()", "Finished");

            return rain;

        } catch (Exception e) {
            // Log.e("parseXmlConductivity EXCPT", e.toString());
            return null;
        }

    }

////////////////////////////////////////////
    ////                                    ////
    //          Water Temperature           //
    ////                                    ////
    ////////////////////////////////////////////


    /**
     * This function reads, parses, and stores the water temperature data from the soap request
     *
     * @param xmlData XML file returned by SOAP Request
     * @param city    Location of request
     */
    public void loadDbWaterTempFromWebService(String xmlData, String city) {

        /////////////////////////////////
        // Parse Water Temperature XML //
        /////////////////////////////////

        Log.i("loadDbWaterTemp...", xmlData);

        //Parse the items
        MeteorologicalItems items3 = parseXmlWaterTemps(xmlData);

        // Error checking for fault codes
        if (items3.getFaultCode() != "none") { // Log Error
            Log.e("Bar Press->SOAP", "FaultCode = " + items3.getFaultCode());
        }
        // Error checking for > 0 items
        if (items3.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items3.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem3 = items3.get(items3.size() - 1); // last item

            if (checkDuplicate(db_read, "WaterTemp", items3.getCity(), currentItem3.getDate(), currentItem3.getTime()) == false) {
                Log.e("AirTemperature", "checkDuplicate()==false");
                cv.put("City", items3.getCity());
                cv.put("DateTime", currentItem3.getDate() + " " + currentItem3.getTime());
                cv.put("Day", currentItem3.getDay());
                cv.put("Value", currentItem3.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("WaterTemp", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    public MeteorologicalItems parseXmlWaterTemps(String xmlData) {
        //Log.i("parseXmlAirTemps()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            WaterTempParseHandler parse_handler = new WaterTempParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(parse_handler);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems items = parse_handler.getItems();
            //Log.i("parseXmlAirTemps()", "Finished");
            return items;
        } catch (Exception e) {
            Log.e("parseXmlAirTemps", e.toString());
            return null;
        }

    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent air temperature based on specified parameters
     */
    public String getWaterTemperature(String location, String dateBefore, String dateAfter) {
        Log.i("getWaterTemperature()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM WaterTemp WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        //String query="SELECT * FROM AirTemp WHERE City = ? ORDER BY DateTime DESC";
        //String[] variables = new String[]{location, dateBefore, dateAfter};    // rawQuery must not include a trailing ';'
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        Log.i("getWaterTemperature()", "Result = " + result);
        return result;
    }


    //////////////////////////////////////
    ////                              ////
    //        Wind                      //
    ////                              ////
    //////////////////////////////////////


    /**
     * This function reads, parses, and stores the wind temperature data from the soap request
     *
     * @param xmlData XML file returned by SOAP Request
     * @param city    Location of request
     */
    public void loadDbWindFromWebService(String xmlData, String city) {


       // Log.i("loadDbWind...", xmlData);

        //Parse the items
        MeteorologicalItems items3 = parseXmlWind(xmlData);

        // Error checking for fault codes
        if (items3.getFaultCode() != "none") { // Log Error
            Log.e("Bar Press->SOAP", "FaultCode = " + items3.getFaultCode());
        }
        // Error checking for > 0 items
        if (items3.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items3.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem3 = items3.get(items3.size() - 1); // last item

            if (checkDuplicate(db_read, "Wind", items3.getCity(), currentItem3.getDate(), currentItem3.getTime()) == false) {
                //Log.e("Wind", "checkDuplicate()==false");
                cv.put("City", items3.getCity());
                cv.put("DateTime", currentItem3.getDate() + " " + currentItem3.getTime());
                cv.put("Day", currentItem3.getDay());

                cv.put("Speed", currentItem3.getPrimaryValue() );
                cv.put("Direction", currentItem3.getPrimaryValue2() );
                cv.put("Gust", currentItem3.getPrimaryValue3() );

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("Wind", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    public MeteorologicalItems parseXmlWind(String xmlData) {
        //Log.i("parseXmlAirTemps()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            WindParseHandler parse_handler = new WindParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(parse_handler);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems items = parse_handler.getItems();
            //Log.i("parseXmlAirTemps()", "Finished");
            return items;
        } catch (Exception e) {
            //Log.e("parseXmlWind", e.toString());
            return null;
        }

    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent air temperature based on specified parameters
     */
    public String getWind(String location, String dateBefore, String dateAfter) {
        Log.i("Wind()", "start");
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM Wind WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects
        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        String result2 = "Data Base Error";
        String result3 = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Speed"));
            result2 = cursor.getString(cursor.getColumnIndex("Direction"));
            result3 = cursor.getString(cursor.getColumnIndex("Gust"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        Log.i("getWind()", "Result = " + result + " " + result2 + " " + result3);
        return result + "," + result2 + "," + result3;
    }


    //////////////////////////////////////
    ////                              ////
    //        Visibility                //
    ////                              ////
    //////////////////////////////////////



    /**
     * This function reads, parses, and stores the water temperature data from the soap request
     *
     * @param xmlData XML file returned by SOAP Request
     * @param city    Location of request
     */
    public void loadDbVisibilityFromWebService(String xmlData, String city) {

        /////////////////////////////////
        // Parse Water Temperature XML //
        /////////////////////////////////

        Log.i("loadVisibilitydata...", xmlData);

        //Parse the items
        MeteorologicalItems items3 = parseXmlVisibility(xmlData);

        // Error checking for fault codes
        if (items3.getFaultCode() != "none") { // Log Error
            Log.e("Bar Press->SOAP", "FaultCode = " + items3.getFaultCode());
        }
        // Error checking for > 0 items
        if (items3.size() > 0) // Verify Parsing yielded at least 1 item
        {
            //add the city code
            items3.setCity(city);    // This field isn't in the xml file, so we add it here

            // Initialize database
            SQLiteHelper helper = new SQLiteHelper(context);
            SQLiteDatabase db_read = helper.getReadableDatabase();

            // Put weather forecast in the database
            ContentValues cv = new ContentValues();

            // Only Care about most recent MeteorologicalItem
            MeteorologicalItem currentItem3 = items3.get(items3.size() - 1); // last item

            if (checkDuplicate(db_read, "Visibility", items3.getCity(), currentItem3.getDate(), currentItem3.getTime()) == false) {
                cv.put("City", items3.getCity());
                cv.put("DateTime", currentItem3.getDate() + " " + currentItem3.getTime());
                cv.put("Day", currentItem3.getDay());
                cv.put("Value", currentItem3.getPrimaryValue());

                SQLiteDatabase db_write = helper.getWritableDatabase(); // Get a writeable Database
                float dbwrite = db_write.insert("Visibility", null, cv); // Write Insert to Database
                Log.e("db_write.insert()", Float.toString(dbwrite));
                db_write.close(); // Finally Close the Database
            }

        }
    }

    public MeteorologicalItems parseXmlVisibility(String xmlData) {
        //Log.i("parseXmlAirTemps()", "Start...");
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            VisibilityParseHandler parse_handler = new VisibilityParseHandler();

            //Its making me cast this as a content handler
            xmlreader.setContentHandler(parse_handler);
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // return a forecast object which contains a list of parsed weather data
            MeteorologicalItems items = parse_handler.getItems();
            //Log.i("parseXmlAirTemps()", "Finished");
            return items;
        } catch (Exception e) {
           // Log.e("parseXmlAirTemps", e.toString());
            return null;
        }

    }

    /**
     * @param location   Location of request
     * @param dateBefore starting date range of request
     * @param dateAfter  ending date range of request
     * @return the most current/recent air temperature based on specified parameters
     */
    public String getVisibility(String location, String dateBefore, String dateAfter) {
        // Initialize the database
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM Visibility WHERE City = ? AND DateTime >= ? ORDER BY DateTime DESC"; // selects

        String[] variables = new String[]{location, dateBefore};
        Cursor cursor = db.rawQuery(query, variables);

        PrintCursor(cursor);

        String result = "Data Base Error";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Value"));
            Log.e("getPrimaryValue()", result);
        }
        cursor.close();
        Log.i("getWaterTemperature()", "Result = " + result);
        return result;
    }

    ///////////////////////////////////////
    ////         Print                  ////
    //                                   //
    ////                               ////
    ///////////////////////////////////////

    public Boolean PrintCursor(Cursor allRows) {
        String tableString = String.format("Table %s:\n", "...");
        if (allRows.moveToFirst()) {
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name : columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
            System.out.println(tableString);
            return true;
        } else {
            return false;
        } // Returns false if above fails

    }





}