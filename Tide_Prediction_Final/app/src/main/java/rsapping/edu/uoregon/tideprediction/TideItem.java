package rsapping.edu.uoregon.tideprediction;

import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("SimpleDateFormat")

/**
 * Created by Alphadog1939 on 7/6/16.
 */
public class TideItem {


   //All the stuff for each tide (all variables)
    //private String tide = null;
    private String date = null;
    private String day = null;
    private String predictionsft = null;
    private String predictionscm = null;
    private String highLow = null;
    private String time = null;
    private String location = null;



    //Setters and getters for them all
    public void setDate(String tempDate) {

        String[] dateStrings = tempDate.split("/");
        String m = (dateStrings[0]);
        String d = (dateStrings[1]);
        String y = (dateStrings[2]);

        tempDate = y +"/"+ m +"/"+ d;
        this.date = tempDate;
    }

    public String getDate() {
        return date;
    }


    public void setDay(String tempDay) {

        String[] dateStrings = tempDay.split("/");
        int m = Integer.parseInt(dateStrings[0]);
        int d = Integer.parseInt(dateStrings[1]);
        int y = Integer.parseInt(dateStrings[2]);

        m = m+1;
        Calendar calendar = new GregorianCalendar(y,m,d);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        dayOfWeek+=1;

        switch (dayOfWeek) {
            case 1:
                day = "Sun";
                break;
            case 2:
                day = "Mon";
                break;
            case 3:
                day = "Tue";
                break;
            case 4:
                day = "Wed";
                break;
            case 5:
                day = "Thur";
                break;
            case 6:
                day = "Fri";

                break;
            case 7:
                day = "Sat";
                break;
            case 8:
                day = "Sun";
                break;
        }
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
        float feet = Float.valueOf(tempDate) * 3.28084f;

        setPredictionsft(Float.toString(feet));

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


    /* Now use city in tide Items,
    public void setLocation(String tempLocation) {
        this.location = tempLocation;
    }

    public String getLocation() {
        return location;
    }
    */


    public void setTime(String tempDate) {
        this.time = tempDate;
    }

    public String getTime() {
        return time;
    }






}