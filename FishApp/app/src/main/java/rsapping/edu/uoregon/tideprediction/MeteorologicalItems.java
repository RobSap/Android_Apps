package rsapping.edu.uoregon.tideprediction;

import java.util.ArrayList;

/**
 * Created by joey on 9/23/2016.
 * http://source.android.com/source/code-style.html#follow-field-naming-conventions
 */

public class MeteorologicalItems extends ArrayList<MeteorologicalItem> {

    //Keeps track of all tide items, also add the city to each list (seperate by lists)
    // Default Serial ID
    private static final long serialVersionUID = 1L;

    private String mDataType = null; // What type of meteorological data is it?

    // Info that applies to the whole forecast
    private String mCity = null;
    private String mFaultCode = "none"; // leave this as "none" for some error checking or else change the error checking too


    // Setters and Getters
    public String getmDataType() {        return mDataType;    }
    public void setmDataType(String mDataType) {        this.mDataType = mDataType;    }

    //Holds the code for the tide location
    public void setCity(String city) {
        this.mCity = city;
    }
    public String getCity() {
        return mCity;
    }

    public String getCurrentValue() {
        return this.get(this.size()-1).getPrimaryValue();
    } // returns the most recent temperature

    public String getFaultCode() {        return mFaultCode;    }
    public void setFaultCode(String faultCode) {        this.mFaultCode = faultCode;    }

}
