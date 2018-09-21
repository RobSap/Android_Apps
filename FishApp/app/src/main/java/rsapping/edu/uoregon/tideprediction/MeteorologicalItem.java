package rsapping.edu.uoregon.tideprediction;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by joey on 9/23/2016.
 * Modified by Robert, to many times to count
 */

public class MeteorologicalItem {


    //All the stuff for each tide (all variables)
    //private String tide = null;
    private String date = null;
    private String day = null;

    private String time = null;
    private String mValue = null;
    private String mValue2 = null;
    private String mValue3 = null;
    private Boolean maxExceeded = null;
    private Boolean minExceeded = null;
    private Boolean rocToleranceExceeded = null;
    private String location = null;


    /////////////////////////////////////////////
    //Setters and getters for all data members //
    /////////////////////////////////////////////
    public void setDate(String tempDate) {

        String[] dateStrings = tempDate.split("-");
        String y = (dateStrings[0]);
        String m = (dateStrings[1]);
        String d = (dateStrings[2]);

        tempDate = y +"/"+ m +"/"+ d;
        this.date = tempDate;
    }
    public String getDate() {
        return date;
    }


    public void setDay(String tempDay) {
        String[] dateStrings = tempDay.split("-");
        int y = Integer.parseInt(dateStrings[0]);
        int m = Integer.parseInt(dateStrings[1]);
        int d = Integer.parseInt(dateStrings[2]);

        if(m>0)
            m = m-1;
        else if(m==0) {
            m = 11;
        }

        // Calendar calendar = Calendar.getInstance();
        //int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //dayOfWeek = dayOfWeek + tempDay;

        Calendar calendar =  Calendar.getInstance();
        calendar.set(y,m,d);
        //int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        int dayOfWeek= calendar.get(Calendar.DAY_OF_WEEK);
        //Log.d("Value", String.valueOf(dateStrings));


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

    public void setTime(String ts) {
        this.time = ts;
    }
    public String getTime() {
        return this.time;
    }

    public void setPrimaryValue(String at) { this.mValue = at; }
    public String getPrimaryValue() { return this.mValue; }

    public void setPrimaryValue2(String at) { this.mValue2 = at; }
    public String getPrimaryValue2() { return this.mValue2; }

    public void setPrimaryValue3(String at) { this.mValue3 = at; }
    public String getPrimaryValue3() { return this.mValue3; }

    public void setMaxExceeded(Boolean bool) { this.maxExceeded = bool; }
    public Boolean getMaxExceeded() { return this.maxExceeded; }

    public void setMinExceeded(Boolean bool) { this.minExceeded = bool; }
    public Boolean getMinExceeded() { return this.minExceeded; }

    public void setRocToleranceExceeded(Boolean bool) { this.rocToleranceExceeded = bool; }
    public Boolean getRocToleranceExceeded() { return this.rocToleranceExceeded; }

}