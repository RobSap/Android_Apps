package rsapping.edu.uoregon.tideprediction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;


/**
 * Created by Alphadog1939 on 7/6/16.
 */

//Parses my text
public class ParseHandler extends DefaultHandler {

    //Items to pull from text
    private TideItems tideItems;
    private TideItem item;


    //True/False to see if we read this specific item
    private boolean isDate = false;
    private boolean isHighLow = false;
    private boolean isTime = false;
    private boolean isDay = false;
    private boolean isPredictions_in_ft = false;
    private boolean isPredictions_in_cm = false;


    //Return the items
    public TideItems getFeed() {
        return tideItems;
    }

    //One start create a new one
    public void startDocument() throws SAXException {
        tideItems = new TideItems();
        item = new TideItem();
    }


    //Start element is called first
    //It checks for each new item, that is new listing in the xml file
    //Then it checks for keywords like date, and grabs the date after it sees date in xml
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            item = new TideItem();
            return;
        }
        else if (qName.equals("date")) {
            isDate = true;
            return;
        }
        else if (qName.equals("day")) {
            isDay = true;
            return;
        }
        else if (qName.equals("time")) {
            isTime = true;
            return;
        }
        else if (qName.equals("predictions_in_ft")) {
            isPredictions_in_ft = true;
            return;
        }
        else if (qName.equals("predictions_in_cm")) {
            isPredictions_in_cm = true;
            return;
        }
        else if (qName.equals("highlow")) {
            isHighLow = true;
            return;
        }
    }

    //It ends reading from this specific entry when it sees item
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            tideItems.getItems().add(item);
        }
        return;
    }


    //This sects contion to parse material, if the item is true is saves the data
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
            item.setPredictions_in_ft(s);
            isPredictions_in_ft = false;
        }

        else if (isPredictions_in_cm) {
            item.setPredictions_cm(s);
            isPredictions_in_cm = false;
        }
        else if (isHighLow) {
            item.setHighlow(s);
            isHighLow = false;
        }


    }
}

