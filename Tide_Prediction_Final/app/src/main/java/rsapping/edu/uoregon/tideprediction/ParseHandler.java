package rsapping.edu.uoregon.tideprediction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;


/**
 * Created by Alphadog1939 on 7/6/16.
 */

//Parses my files
public class ParseHandler extends DefaultHandler {


    //Declare everything
    private TideItems tideItems;
    private TideItem item;


    //Initilize booleans
    //Left in legacy code for for old functions from parsing
    //private boolean isDate = false;
    private boolean isHighLow = false;
    private boolean isTime = false;
   // private boolean isDay = false;
    //private boolean isPredictions_in_ft = false;
    private boolean isPredictions_in_cm = false;
    private int day = 0;
    private String holdDate = "";


    //Legacy method that was replaced by getItems
   /*public TideItems getFeed() {
        return tideItems;
    }
    */
    public TideItems getItems() {
        return tideItems;
    }

    //Starts it
    public void startDocument() throws SAXException {
        tideItems = new TideItems();
        item = new TideItem();
    }


    //Start looking for these items
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        //Breaks on this item -- means it tells you each time there is a new item
        if (qName.equals("data")) {
            item = new TideItem();
            return;
        }
        else if (qName.equals("item")) {
            holdDate = atts.getValue("date");
            //isDate = true;
           // return;
        }
        else if (qName.equals("pred")) {
            isPredictions_in_cm = true;
            return;
        }
        // Pulls info on time
        else if (qName.equals("time")) {
            isTime = true;
            return;
        }//Pulls predictions in ft
        //Tells if its high or low tide
        else if (qName.equals("type")) {
            isHighLow = true;
            return;
        }
    }

    //End one come on new item
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("data")) {
            tideItems.add(item);
        }
        return;
    }

    //Sets which word I come acrross (that way I can filter it)
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        /*if (isDate) {
            //holdDate = s;
            //item.setDate(holdDate);
            //item.setDay(day);
            //day += 1;
            isDate = false;
        }*/
         if (isTime) {
            item.setDate(holdDate);
            item.setDay(holdDate);
            item.setTime(s);
            isTime = false;
        }
        else if (isPredictions_in_cm) {
            item.setPredictionscm(s);
            isPredictions_in_cm = false;
        }
        else if (isHighLow) {
            item.setHighlow(s);
            isHighLow = false;
        }


    }
}

