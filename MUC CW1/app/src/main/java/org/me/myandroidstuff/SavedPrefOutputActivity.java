package org.me.myandroidstuff;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Graham on 16/12/2015.
 */
public class SavedPrefOutputActivity extends Activity implements View.OnClickListener {

    FragmentManager fmAboutDialogue;                // Lab 2 - Fragment Manager
    SharedPreferences mcSharedPrefs;
    Button btnBack;

    TextView tvCity; // lab 4
    TextView tvPostCode; // lab 4
    TextView tvRegion; // lab 4

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_output_screen); //app main UI screen

        fmAboutDialogue = this.getFragmentManager();

        //Display Text

        Intent intent = getIntent();
        ///////////////

        tvCity = (TextView) findViewById(R.id.city_out); // Lab 4
        tvPostCode = (TextView) findViewById(R.id.post_out); // Lab 4
        tvRegion = (TextView) findViewById(R.id.region_out); // Lab 4
        btnBack = (Button) findViewById(R.id.btnBack);

        //load any saved preferences
        mcSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadSavedPreferences();
        Log.e("n", "SDOutput msg");
    }

    public void onClick(View view) {

        if(view == btnBack)
        {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

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

    private void loadSavedPreferences(){

        tvCity.setText("City:" + tvCity.getText() + mcSharedPrefs.getString("City", "Stirling"));
        tvPostCode.setText("PostCode:" + tvPostCode.getText() + mcSharedPrefs.getString("PostCode", "FK95SE"));
        tvRegion.setText("Region:" + mcSharedPrefs.getString("Region", "StirlingShire"));
    }
}
