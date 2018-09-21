package rsapping.edu.uoregon.tideprediction;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Robert
 */

public class WindParseHandler extends DefaultHandler {
    //Declare everything
    private MeteorologicalItems mItems;
    private MeteorologicalItem mItem;

    private int day = 0;
    private String holdTimeStamp = "";

    // Set Tag Flags
    Boolean isTimeStamp = false;
    Boolean isTemp = false;
    Boolean isX = false;
    Boolean isWS = false;
    Boolean isWD = false;
    Boolean isWG = false;
    Boolean isN = false;
    Boolean isR = false;
    int size = 0;

    private boolean isFaultCode = false;


    public MeteorologicalItems getItems() {
        return this.mItems;
    }

    //Starts it
    public void startDocument() throws SAXException {
        mItems = new MeteorologicalItems();
        mItem = new MeteorologicalItem();
    }

    //Start looking for these items
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        //Breaks on this mItem -- means it tells you each time there is a new mItem
        if (qName.equals("item")) {
            mItem = new MeteorologicalItem();
            return;
        }
        else if (qName.equals("timeStamp")) {
            isTimeStamp = true;
            return;
        }
        else if (qName.equals("WS")) {
            isWS = true;
            return;
        }
        else if (qName.equals("WD")) {
            isWD = true;
            return;
        }
        else if (qName.equals("WG")) {
            isWG = true;
            return;
        }
        else if (qName.equals("X")) {
            isX = true;
            return;
        }
        else if (qName.equals("R")) {
            isR = true;
            return;
        }
        else if (qName.equals("faultstring")) {
            isFaultCode = true;
            return;
        }
    }

    //End one come on new mItem
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            size ++;
            if(size < 103)
            {Log.e("here", "");
                mItems.add(mItem);}
            else {
                Log.e("here", "");
                mItems.add(mItem);
            }

        }
        return;
    }

    //Sets which word I come acrross (that way I can filter it)
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        if (isTimeStamp) {
            String[] split = s.split("\\s+");
            if (split.length != 2) {
                Log.e("Parsing TimeStamp>>", "splitting timestamp expected 2 parts, but instead got "+split.length+ " parts");
            }
            // Set variables from timestamp
            mItem.setDate(split[0]);
            mItem.setDay(split[0]);
            mItem.setTime(split[1]);
            isTimeStamp = false;
        }
        else if (isTemp) {
            //mItem.setPrimaryValue(s);
            isTemp = false;
        }
        else if (isWS) {
            mItem.setPrimaryValue(s);
            isWS = false;
        }
        else if (isWD) {
            mItem.setPrimaryValue2(s);
            isWD = false;
        }
        else if (isWG) {
            mItem.setPrimaryValue3(s);
            isWG = false;
        }
        else if (isX) {
            mItem.setMaxExceeded(Boolean.parseBoolean(s));
            isX = false;
        }
        else if (isR) {
            mItem.setRocToleranceExceeded(Boolean.parseBoolean(s));
            isR = false;
        }
        else if (isFaultCode) {
            mItems.setFaultCode(s);
            isFaultCode = false;
        }

    }

}
