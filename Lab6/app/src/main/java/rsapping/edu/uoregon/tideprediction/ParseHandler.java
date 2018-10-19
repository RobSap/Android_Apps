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

    private TideItems tideItems;
    private TideItem item;


    private boolean isDate = false;
    private boolean isHighLow = false;
    private boolean isTime = false;
    private boolean isDay = false;
    private boolean isPredictions_in_ft = false;
    private boolean isPredictions_in_cm = false;


    public TideItems getFeed() {
        return tideItems;
    }
    public TideItems getItems() {
        return tideItems;
    }

    public void startDocument() throws SAXException {
        tideItems = new TideItems();
        item = new TideItem();
    }


    //Start looking for these items
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        //Breaks on this item -- means it tells you each time there is a new item
        if (qName.equals("item")) {
            item = new TideItem();
            return;
        } //Pulls info on date
        else if (qName.equals("date")) {
            isDate = true;
            return;
        } //Pulls info on day
        else if (qName.equals("day")) {
            isDay = true;
            return;
        }//Pulls info on time
        else if (qName.equals("time")) {
            isTime = true;
            return;
        }//Pulls predictions in ft
        else if (qName.equals("predictions_in_ft")) {
            isPredictions_in_ft = true;
            return;
        }//Pulls predictions in cm
        else if (qName.equals("predictions_in_cm")) {
            isPredictions_in_cm = true;
            return;
        }//Tells if its high or low tide
        else if (qName.equals("highlow")) {
            isHighLow = true;
            return;
        }
    }

    //End one come on new item
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            tideItems.add(item);
        }
        return;
    }

    //Sets which word I come acrros (that way I can filter it)
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        if (isDate) {
            item.setDate(s);
            isDate = false;
        }

      else if (isDay) {
            item.setDay(s);
            isDay = false;
        }
        else if (isTime) {
            item.setTime(s);
            isTime = false;
        }
        else if (isPredictions_in_ft) {
            item.setPredictionsft(s);
            isPredictions_in_ft = false;
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

