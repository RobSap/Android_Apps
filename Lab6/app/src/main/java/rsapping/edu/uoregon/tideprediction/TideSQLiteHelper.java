package rsapping.edu.uoregon.tideprediction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alphadog1939 on 7/9/16.
 */


public class TideSQLiteHelper extends SQLiteOpenHelper {

    //Name of my database
    private static final String DATABASE_NAME = "tides.sqlite";
    private static final int DATABASE_VERSION = 1;
    private Context context = null;

    //Default constructor
    public TideSQLiteHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = c;
    }

    //Creates my Database
    @Override
    public void onCreate(SQLiteDatabase db) {
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
    }

    //Used to update, I shouldn't need to update because it works, So I didn't implement this
    //I left this in because it complained otherwise
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
