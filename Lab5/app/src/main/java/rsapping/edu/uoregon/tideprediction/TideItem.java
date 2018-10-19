package rsapping.edu.uoregon.tideprediction;

import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")

/**
 * Created by Alphadog1939 on 7/6/16.
 */
public class TideItem {


   //All my tide items to save , that is all the info like the date of a tide
    private String tide = null;
    private String date = null;
    private String day = null;
    private String predictions_ft = null;
    private String predictions_cm = null;
    private String highLow = null;
    private String time = null;


    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");


    //All my setters and getters for stuff im keeeping track of , like the date
    public void setDate(String tempDate) {
        this.date = tempDate;
    }

    public String getDate() {
        return date;
    }



    public void setDay(String tempDay) {
        this.day = tempDay;
    }

    public String getDay() {
        return day;
    }




    public void setPredictions_in_ft(String tempDate) {
        this.predictions_ft = tempDate;
    }

    public String getPredictions_in_ft() {
        return predictions_ft;
    }


    public void setPredictions_cm(String tempDate) {
        this.predictions_cm = tempDate;
    }

    public String getPredictions_cm() {
        return predictions_cm;
    }


    public void setHighlow(String highLow) {
        if((highLow).equals("H"))
            this.highLow = "High";
        else
            this.highLow = "Low";
    }

    public String getHighLow() {
        return highLow;
    }

    public void setTime(String tempDate) {
        this.time = tempDate;
    }

    public String getTime() {
        return time;
    }



}