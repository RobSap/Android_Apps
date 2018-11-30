package rsapping.edu.uoregon.tideprediction;

import java.util.ArrayList;

/**
 * Created by Alphadog1939 on 7/6/16.
 */

//This keeps a list of my tide item, in an tideitems list

public class TideItems {
    private ArrayList<TideItem> items =
            new ArrayList<TideItem>();

    public ArrayList<TideItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TideItem> items) {
        this.items = items;
    }
}
