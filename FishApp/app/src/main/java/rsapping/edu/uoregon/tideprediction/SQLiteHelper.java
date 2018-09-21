package rsapping.edu.uoregon.tideprediction;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 Database Standards:
 - SQL Naming Standards: http://www.isbe.net/ILDS/pdf/SQL_server_standards.pdf
 */


public class SQLiteHelper extends SQLiteOpenHelper {

    //Name of my database
    private static final String DATABASE_NAME = "tides.sqlite";
    private static final int DATABASE_VERSION = 1;
    private Context context = null;

    //Default constructor
    public SQLiteHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = c;
    }

    //Creates my Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        //==========================//
        //        Tide Tables       //
        //==========================//

        // Tide Prediction Table
        db.execSQL("CREATE TABLE Tides"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "Date TEXT,"
                + "Day TEXT,"
                + "Predictionsft TEXT,"
                + "Predictionscm TEXT,"
                + "HighLow TEXT,"
                + "Time TEXT"
                + ")" );

        //=====================================//
        //        Meteorological Tables        //
        //=====================================//
        // 1. Air Temperature
        // 2. Water Temperature
        // 3. Barometric Pressure
        // 4. Rain Fall
        // 5. Humidity
        // 6. Visibility
        // 7. Wind (speed, direction, gustSpeed)
        //=====================================//

        //  Create Air Temperature Table //
        db.execSQL("CREATE TABLE AirTemp"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Water Temperature Table //
        db.execSQL("CREATE TABLE WaterTemp"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Barometric Pressure Table //
        db.execSQL("CREATE TABLE BarPress"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Rain Fall Table //
        db.execSQL("CREATE TABLE RainFall"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Humidity Table //
        db.execSQL("CREATE TABLE Humidity"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Visibility Table //
        db.execSQL("CREATE TABLE Visibility"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Value REAL"
                + ")" );

        // Create Wind Table //
        db.execSQL("CREATE TABLE Wind"
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "City TEXT,"
                + "DateTime TEXT,"
                + "Day TEXT,"
                + "Speed REAL,"
                + "Direction REAL,"
                + "Gust REAL" // speed of gusts in M/Second
                + ")" );

    }

    //Used to update, I shouldn't need to update because it works, So I didn't implement this
    //I left this in because it complained otherwise
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
