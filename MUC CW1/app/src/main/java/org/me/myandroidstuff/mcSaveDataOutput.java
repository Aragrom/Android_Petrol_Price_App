package org.me.myandroidstuff;

/**
 * Created by Graham on 12/12/2015.
 */
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
import org.me.myandroidstuff.PetrolPriceActivity;

/**
 * Created by Graham on 08/10/2015.
 */
public class mcSaveDataOutput extends Activity implements View.OnClickListener {

    SharedPreferences mcSharedPrefs;
    Button btnBack;
    QueryObject queryObject;
    TextView tvDiesel;
    TextView tvUnleaded;
    TextView tvSuperUnleaded;
    TextView tvLPG;
    TextView tvLRP;
    FragmentManager fmAboutDialogue;                // Lab 2 - Fragment Manager

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_display_shared_prefs); // app main UI screen
        fmAboutDialogue = this.getFragmentManager();

        //setup, access and listen for the back button
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        //create text view for output
        tvDiesel = (TextView) findViewById(R.id.diesel_out);
        tvUnleaded = (TextView) findViewById(R.id.unleaded_out);
        tvSuperUnleaded = (TextView) findViewById(R.id.super_unleaded_out);
        tvLPG = (TextView) findViewById(R.id.LPG_out);
        tvLRP = (TextView) findViewById(R.id.LRP_out);

        //load any saved preferences
        mcSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.e("n", "SDOutput msg");
        Intent iMainAct = getIntent();
        queryObject = (QueryObject)iMainAct.getSerializableExtra("queryObject");
        loadPricesPreferences();
    }

    public void onClick(View view){

        setResult(Activity.RESULT_OK);
        finish();
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

    private void loadPricesPreferences()
    {
        tvDiesel.setText(queryObject.getDieselPrices());
        tvUnleaded.setText(queryObject.getUnleadedPrices());
        tvSuperUnleaded.setText(queryObject.getSuperUnleadedPrices());
        tvLPG.setText(queryObject.getLPGPrices());
        tvLRP.setText(queryObject.getLRPPrices());
    }
}
