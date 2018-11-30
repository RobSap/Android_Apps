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


   //All the stuff for each tide (all variables)
    private String tide = null;
    private String date = null;
    private String day = null;
    private String predictionsft = null;
    private String predictionscm = null;
    private String highLow = null;
    private String time = null;


    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");



    //Setters and getters for them all
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



    public void setPredictionsft(String tempDate) {
        this.predictionsft = tempDate;
    }

    public String getPredictionsft() {
        return predictionsft;
    }


    public void setPredictionscm(String tempDate) {
        this.predictionscm = tempDate;
    }

    public String getPredictionscm() {
        return predictionscm;
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