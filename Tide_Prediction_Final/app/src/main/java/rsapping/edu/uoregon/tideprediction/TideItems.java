package rsapping.edu.uoregon.tideprediction;

import java.util.ArrayList;

/**
 * Created by Alphadog1939 on 7/6/16.
 */
public class TideItems extends ArrayList<TideItem>{

    //Keeps track of all tide items, also add the city to each list (seperate by lists)
    // Default Serial ID
    private static final long serialVersionUID = 1L;

    // Info that applies to the whole forecast
    private String city = "";


    //Holds the code for the tide location
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
