package org.me.myandroidstuff;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/*
 * ============================
 * Parser object creates queryObject and parses a string into data
 * - Input is a single string
 * - events on html tags trigger allocation of data
 * - this data is put into the queryObject using 'Type' and 'Rate'
 *============================
 */	
public class Parser {
	
	// Only Method
	public QueryObject parseData(String dataToParse)
	{
		QueryObject newQuery = new QueryObject();
		String strFuelType = new String();
		
		try
		{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput( new StringReader ( dataToParse ) );
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) 
			{				
				// Found a start tag 
				if(eventType == XmlPullParser.START_TAG) 
				{
					
					if (xpp.getName().equalsIgnoreCase("fuel"))
					{
						strFuelType = xpp.getAttributeValue(null, "type");
					}
					
					// Check which Tag we have
					//if (xpp.getName().equalsIgnoreCase("highest units"))
					if (xpp.getName().equalsIgnoreCase("highest")
							|| xpp.getName().equalsIgnoreCase("average")
							|| xpp.getName().equalsIgnoreCase("lowest"))
            		{
						// Now just get the associated text 
						String strRate = xpp.getName();
						String strData = xpp.nextText();
						
						Log.e("Parser", strFuelType + " " + strRate + " " + strData);
						newQuery.addData(strFuelType, strRate, strData);
            		}
				}
						
				// Get the next event	
				eventType = xpp.next();
				
			} // End of while
		}
 		catch (XmlPullParserException ae1)
 		{
 			Log.e("Parser","Parsing error" + ae1.toString());
 		}
 		catch (IOException ae1)
 		{
 			Log.e("Parser","IO error during parsing");
 		}
		
		return newQuery;
	}
}
