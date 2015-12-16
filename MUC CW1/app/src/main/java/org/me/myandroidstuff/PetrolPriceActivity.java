package org.me.myandroidstuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;

/*
 * ============================
 * Taking user input query an RSS stream displaying the returned data
 * for petrol pricing
 *============================
 */
public class PetrolPriceActivity extends Activity implements View.OnClickListener
{
	FragmentManager fmAboutDialogue;                // Lab 2 - Fragment Manager
    SharedPreferences mcSharedPrefs;                // Stores users data as saved prefs
    SavedPrefActivity savedPrefActivity;            // Activity to hold the values for users Preferences
    private Button displaySaved;                    // Button that onClick() launches SavedPrefActivity
    private Button displayCavnas;                   // Button that onClick() launches CanvasActivity
    private Button displayPrices;                   // Button that onClick() launches QueryOutputActivity

	private TextView response;						// for testing - for displaying full string from RSS stream

    private Button searchButton;					// UI Component using onClickListener activate "main program loop"
    private ProgressBar progressBar;				//     "  "     for displaying the progress of the download
    private TextView txt_percentage;				//     "  "     for displaying percentage of progressBar UI component (connection state)
    
    private Spinner spinSearchType;					//     "  "     takes input and uses for search type for RSS stream query
    private EditText editSearchValue;				//     "  "      UI Component takes input and uses for value type for RSS stream query
    private ListView listView;
    
    private String strResult;						// hold the value returned from RSS stream
    public QueryObject queryObject;				    // Data Structure (Object) for holding and handling petrol pricing data
    private ArrayAdapter<String> adapter;			// Loads data from queryObject into the listView
    private Parser parser = new Parser();			// Object that creates a queryObject then parses data into it
    
    private int iListViewScrollDuration = 50000;	// Automated scroll duration for listView
    
    
    /*
     * ============================
	 * For handling orientation and reseting of the application
	 *============================
	 */
    private ProgressBar savedBar;					// Tracking progress bar
    private EditText savedEditText;					//   "  "   user input value for search
    private Spinner savedSpinner;					//   "  "      "  "    type for search
    private ArrayAdapter<String> savedAdapter;		//   "  "   data from queryObject
    private TextView savedTextView;					//   "  "   progress text
    
    // Used to track state changes in HandleReload()
    private boolean bHasResult = false;				// Manages updates for input for search and value and price list for listView
    private boolean bDownloading = false;			//   "  "    "  "  for progress bar and text
    //============================
    
    
    /*
     * ============================
	 * For testing - Expected result from stream
	 *============================
	 */
    private String petrolString = 
    		  "<PetrolPrices>"
    		+ "<Fuel type=\"Unleaded\">"
    		+ "<Highest units=\"p\">137.9</Highest>"
    		+ "<Average units=\"p\">133.9</Average>"
    		+ "<Lowest units=\"p\">130.9</Lowest>"
    		+ "</Fuel>"
    		+ "<Fuel type=\"Diesel\">"
    		+ "<Highest units=\"p\">141.9</Highest>"
    		+ "<Average units=\"p\">138.6</Average>"
    		+ "<Lowest units=\"p\">136.7</Lowest>"
    		+ "</Fuel>"
    		+ "<Fuel type=\"Super Unleaded\">"
    		+ "<Highest units=\"p\">146.9</Highest>"
    		+ "<Average units=\"p\">141.5</Average>"
    		+ "<Lowest units=\"p\">136.9</Lowest>"
    		+ "</Fuel>"
    		+ "<Link>"
    		+ "http://www.petrolprices.com/search.html?search=Glasgow"
    		+ "</Link>"
    		+ "</PetrolPrices>";
    
    private String[] aStrPrices = {"137.9","133.9","130.9",
    		"141.9","138.6","136.7",
    		"146.9","141.5","136.9"};
    //============================
    
    
    
    /** Called when the activity is first created. */
    @Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Create a a new queryObject for storing object for storing petrol prices
        queryObject = new QueryObject();
        // Link spinner with layout
    	savedSpinner = spinSearchType = (Spinner)findViewById(R.id.type);

        // Create About dialog
        fmAboutDialogue = this.getFragmentManager();

        // Create Default preferences for the app.
        mcSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        savedPrefActivity = new SavedPrefActivity(mcSharedPrefs);

        savedPrefActivity.setDefaultPrefs();                    // Set the default prefs
        setUpUI();
    } // End of onCreate
    
    
    
    /*
     * ============================
	 * Initialize UI components
	 *============================
	 */	
    private void setUpUI()
    {
    	setContentView(R.layout.main);
        displayCavnas = (Button) findViewById(R.id.btn_canvas);
        displayCavnas.setOnClickListener(this);
        displayPrices = (Button) findViewById(R.id.btn_prices);
        displayPrices.setOnClickListener(this);
        displaySaved = (Button) findViewById(R.id.btnSavedData);
        displaySaved.setOnClickListener(this);
        searchButton = (Button) findViewById(R.id.btn_search);
        progressBar =  (ProgressBar) findViewById(R.id.progress);
        txt_percentage= (TextView) findViewById(R.id.txt_percentage);
        searchButton.setOnClickListener(this); 
        
        // Get the TextView object on which to display the results
        response = (TextView)findViewById(R.id.urlResponse);
        spinSearchType = (Spinner)findViewById(R.id.type);
        editSearchValue = (EditText)findViewById(R.id.value);
        
        listView = (ListView)findViewById(R.id.list_view);        
     	listView.setAdapter(adapter);
     	
    }// End of setUpUI
    
    
    
    /*
     * ============================
	 * Handle orientation changes
	 *============================
	 */	
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        saveResources();	// Save as back-up values
        setUpUI();			// Reset values
        handleReload();		// Reassign values
        
    }// End of onConfigurationChanged
    
    
    
    /*============================
	 * Saves resources
	 *============================
	 */	
    private void saveResources()
    {
    	savedEditText = editSearchValue;	// input search value
    	savedSpinner = spinSearchType;		// input search type
    	
    	savedAdapter = adapter;				// data from queryObject (Petrol Prices)
    	
    	if(bDownloading) // In progress
    	{
    		savedBar = progressBar;			// Downloading/connecting progress bar
    		savedTextView = txt_percentage; //    "  "       "  "     progress text
    	}
    	
    }// End of saveResources
    
    
    
    /*
     * ============================
	 * Reloads fields for Search Value and List View
	 *============================
	 */
    public void handleReload()
    {
    	//Loading "Search Value" for edit text from saved string
    	editSearchValue.setText(savedEditText.getText().toString());
    	
    	//load Spinner value from saved spinner
    	spinSearchType.setSelection(savedSpinner.getSelectedItemPosition());
    	
    	searchButton.setEnabled(true);
        displaySaved.setEnabled(true);
        displayPrices.setEnabled(true);
        displayCavnas.setEnabled(true);
    	
    	Log.e("PetrolPriceActivity", "Reloading: Input 'Value'(Text Box) and 'Type'(Spinner)");
    	
    	if(bDownloading) // Set true onClick - 'search' button
    	{
    		progressBar.setProgress(savedBar.getProgress());
    		progressBar.setVisibility(View.VISIBLE);
    		txt_percentage.setText(savedTextView.getText());
    		searchButton.setEnabled(false); // So multiple searches are not activated
            displaySaved.setEnabled(false);
            displayCavnas.setEnabled(false);
            displayPrices.setEnabled(false);
    	}
    	
    	if(bHasResult) // Set true once a first search has been successful
    	{
    		if(!bDownloading) txt_percentage.setText("Download complete");         	
         	listView.setAdapter(savedAdapter);
         	scrollDownListView();         	
         	Log.e("PetrolPriceActivity", "Reloading: listView (Also Calling function 'ScrollDownListView')");
    	}
    	
    }// End of handleReload
    
    
    
    /*
     * ============================
	 * Click listener
	 *============================
	 */
    public void onClick(View aview) 
	{
    	//if click was on search button
		if (aview == searchButton)
		{
			// If internet connection is established
			if(isOnline()){

                // Disable buttons from being clicked
				searchButton.setEnabled(false);
                displaySaved.setEnabled(false);
                displayCavnas.setEnabled(false);
                displayPrices.setEnabled(false);

                // Make progress bar and text visible
				progressBar.setVisibility(View.VISIBLE);
                txt_percentage.setVisibility(View.VISIBLE);

                // execute
				new customAsyncTask().execute();

                Log.e("PetrolPriceActivity", "About to launch database");
                // Create database handler instance
                DataBaseManager dbMgr = new DataBaseManager(this, "newDataBase.s3db",null,1); // Lab 4
                try {
                    dbMgr.dbCreate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("PetrolPriceActivity", "Got through database!!!");

                bHasResult = true;
			}
			
			// If not online display this information
			if(!isOnline()){
				Toast.makeText(PetrolPriceActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
			}
		}

        // if click was to display saved preferences
        if(aview == displaySaved)
        {
            // Save preferences based on selection
            if(String.valueOf(spinSearchType.getSelectedItem()) == "Town") savedPrefActivity.savePreferences("City", String.valueOf(spinSearchType.getSelectedItem()));
            else if(String.valueOf(spinSearchType.getSelectedItem()) == "Postcode") savedPrefActivity.savePreferences("PostCode", String.valueOf(spinSearchType.getSelectedItem()));
            else if(String.valueOf(spinSearchType.getSelectedItem()) == "County") savedPrefActivity.savePreferences("Region", String.valueOf(spinSearchType.getSelectedItem()));

            Log.e("PetrolPriceActivity",String.valueOf(spinSearchType.getSelectedItem()));

            //Starting a new Intent
            Intent savedPrefsOutputActivity = new Intent(getApplicationContext(), SavedPrefOutputActivity.class);

            //Log the output data
            Log.e("PetrolPriceActivity", "Got through create about to Prefs output screen");

            startActivity(savedPrefsOutputActivity);
            //finish();
        }

        // if click was to display prices from query
        if(aview == displayPrices)
        {
            //Starting a new Intent
            Intent queryOutputActivity = new Intent(getApplicationContext(), QueryOutputActivity.class);

            //Send data to the new Activity
            //sOutputMsg = "TEST";
            queryOutputActivity.putExtra("queryObject", queryObject); //Lab 3

            //Log the output data
            Log.e("PetrolPriceActivity", "Got through create about to start Price output screen");

            startActivity(queryOutputActivity);
        }

        // if click was to draw the price history to the canvas
        if(aview == displayCavnas)
        {
            Intent intentDraw = new Intent(this, CanvasActivity.class);
            this.startActivity(intentDraw);
        }
		
	}// End of onClick
    
    
    
    /*
     * ============================
	 * Returns a string using input value and type from user representing a URL
	 * For use in RSS stream
	 *============================
	 */
    private String getStringURL()
    {
    	String strResultUrl; 																	// to be returned
    	
    	String strSearchType = String.valueOf(spinSearchType.getSelectedItem()); 				// user input type ("Town")
    	String strSearchValue = editSearchValue.getText().toString();							// user input value ("Glasgow")
    	
    	// used to build up the body of the url
    	String strStartUrl = "http://www.petrolprices.com/feeds/averages.xml?search_type=";
    	String strMidUrl = "&search_value=";

    	// String addition - building the full url and returning it
    	return strResultUrl = strStartUrl + strSearchType + strMidUrl + strSearchValue;
    	
    }// End of getStringURL
    
    
    
    /*
     * ============================
	 * Returns a string from an RSS stream
	 *============================
	 */
    // Method to handle the reading of the data from the RSS stream
    private static String petrolPriceString(String urlString)throws IOException
    {
	 	String result = "";
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(urlString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Throw away the header
    			bin.readLine();
    			// Read in the data from the RSS stream
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + "\n" + line;
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    			throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	return result;
    	
    }// End of petrolPriceString
    
    
    
    /*
     * ============================
	 * Class - Main application loop (done in background)
	 *============================
	 */
    private class customAsyncTask extends AsyncTask<Void, Integer, Void>
    {
 
     int progress_status;
      
     @Override
     protected void onPreExecute() 
     {
    	 // update the UI immediately after the task is executed
    	 super.onPreExecute();
    	 
    	 //incase of orientation change keep state
    	 bDownloading = true;
    
    	 Toast.makeText(PetrolPriceActivity.this,"Connecting", Toast.LENGTH_SHORT).show();
 
    	 progress_status = 0;
    	 txt_percentage.setText("Connecting...");
    
     }// End of onPreExecute
      
     @Override
     protected Void doInBackground(Void... params) 
     {
    	 
    	 try
         {
         	// Get the data from the RSS stream as a string
         	strResult =  petrolPriceString(getStringURL());
         	
         	//Parse data using parser
         	queryObject = parser.parseData(strResult);
         }
         catch(IOException ae)
         {
         	// Handle error
         	response.setText("Error");
         }
    	 
    	 while(progress_status<100)
    	 {
     
    		 progress_status += 1;
     
    		 publishProgress(progress_status);
    		 // Sleep but normally do something useful here such as access a web resource
    		 //SystemClock.sleep(50); // or Thread.sleep(300);
    		 
    		 // Really need to do some calculation on progress
    	 }
    	 
    	 return null;
    	 
     }// End of doInBackground
  
     @Override
     protected void onProgressUpdate(Integer... values) 
     {
    	 super.onProgressUpdate(values);
    
    	 progressBar.setProgress(values[0]);                        // set progress bar value
    	 
    	 txt_percentage.setText("Downloading " +values[0] + "%");   // update text message
    
     }
   
     @Override
     protected void onPostExecute(Void result) 
     {
    	 super.onPostExecute(result);
    
    	 Toast.makeText(PetrolPriceActivity.this,
            "Downloaded", Toast.LENGTH_SHORT).show();
     
    	 txt_percentage.setText("Download complete");

         // Make buttons clickable
    	 searchButton.setEnabled(true);
         displaySaved.setEnabled(true);
         displayCavnas.setEnabled(true);
         displayPrices.setEnabled(true);

         // Hide download bar and text
    	 progressBar.setVisibility(View.INVISIBLE);
         txt_percentage.setVisibility(View.INVISIBLE);
    	 
    	 
    	 //Get data for list view
    	 String[] aStr = queryObject.getArrayOfDataForListView();
		 
    	 // Display the string in the TextView object just to demonstrate this capability
    	 // This will need to be removed at some point
    	 adapter = new ArrayAdapter<String>(PetrolPriceActivity.this, android.R.layout.simple_list_item_1, aStr);
    	 listView.setAdapter(adapter);
    	 bDownloading = false;
	  	
    	 scrollDownListView();
    	 
     }// End of onPostExecute
     
    }// End of customAsyncTask
    
    
    
    /*
     * ============================
	 * Move scroll view to bottom of page
	 *============================
	 */	
    private void scrollDownListView()
    {
    	listView.post(new Runnable() 
    	{ 
			@SuppressLint("NewApi")
			public void run() 
			{
				// ScrollView.FOCUS_DOWN = down direction (direction, speed)
				listView.smoothScrollBy(ScrollView.FOCUS_DOWN + 1000, iListViewScrollDuration); 
			} 
		}); 
    	
    }// End of scrollDownListView
    
    
    
	/*
	 * ============================
	 * isOnline()
	 * Used in onClick() on "Search" button
	 *============================
	 */		
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo.isConnected(); //Return connection state
	    
	}// End of petrolPriceString

	/*
	 * ============================
	 * Inflate menu vith menu items
	 *============================
	 */
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mc_menu, menu);
		return true;
	}

    /*
	 * ============================
	 * Handle menu selection click events
	 *============================
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        // handle menu selected item and displaying dialogue
        switch (item.getItemId()) {
            case R.id.mQuit:
                finish();
                return true;
            case R.id.mAbout:
                DialogFragment mcAboutDlg = new mcAboutDialogue();
                mcAboutDlg.show(fmAboutDialogue, "mc_About_Dlg");
                return true;
            case R.id.mPriceAbout:
                DialogFragment mcPriceDlg = new PriceAboutDialogue();
                mcPriceDlg.show(fmAboutDialogue, "mc_About_Dlg");
                return true;
            case R.id.mPrefAbout:
                DialogFragment mcPrefDlg = new PrefAboutDialogue();
                mcPrefDlg.show(fmAboutDialogue, "mc_About_Dlg");
                return true;
            case R.id.mCanvasAbout:
                DialogFragment mcCanvasDlg = new CanvasAboutDialogue();
                mcCanvasDlg.show(fmAboutDialogue, "mc_About_Dlg");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}
    
}// End of PetrolPriceActivity class