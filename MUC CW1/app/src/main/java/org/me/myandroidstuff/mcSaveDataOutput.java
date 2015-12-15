package org.me.myandroidstuff;

/**
 * Created by Graham on 12/12/2015.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Graham on 08/10/2015.
 */
public class mcSaveDataOutput extends Activity implements View.OnClickListener {

    SharedPreferences mcSharedPrefs;
    Button btnBack;
    TextView tvCity;
    TextView tvPostCode;
    TextView tvRegion;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_display_shared_prefs); // app main UI screen

        //setup, access and listen for the back button
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        //create text view for output
        tvCity = (TextView) findViewById(R.id.city_out);
        tvPostCode = (TextView) findViewById(R.id.post_out);
        tvRegion = (TextView) findViewById(R.id.region_out);

        //load any saved preferences
        mcSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadSavedPreferences();
        Log.e("n", "SDOutput msg");
    }

    public void onClick(View view){

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void loadSavedPreferences(){

        //tvCity.setText(tvCity.getText() + mcSharedPrefs.getString("City", "Stirling"));
        //tvPostCode.setText(tvPostCode.getText() + mcSharedPrefs.getString("PostCode", "FK95SE"));
        //tvRegion.setText(tvRegion.getText() + mcSharedPrefs.getString("Region", "StirlingShire"));
        tvCity.setText("Hello");
        tvPostCode.setText("World");
        tvRegion.setText("Sup");
    }
}
