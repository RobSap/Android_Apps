package rsapping.edu.uoregon.tideprediction;

import android.content.Context;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/**
 * Created by Alphadog1939 on 7/6/16.
 */
public class FileIO {

    //Name of the file to open
    private final String FILENAME = "tides.xml";
    private Context context = null;

    public FileIO (Context context) {
        this.context = context;
    }

    public TideItems readFile() {
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            ParseHandler tideParseHandler = new ParseHandler();
            xmlreader.setContentHandler(tideParseHandler);

            // read the file from internal storage
            InputStream in = context.getAssets().open(FILENAME);

            // parse the data
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            // return a forecast object which contains a list of parsed weather data
           TideItems forecast = tideParseHandler.getFeed();
                return forecast;

        }
        catch (Exception e) {
            Log.e("News reader", e.toString());
            return null;
        }
    }

}
