package org.me.myandroidstuff;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Graham on 12/12/2015.
 */
public class mcOutputScreen extends Activity implements View.OnClickListener {

    //Button btnBack;

    TextView tvCity; // lab 4
    TextView tvPostCode; // lab 4
    TextView tvRegion; // lab 4

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_output_screen); //app main UI screen

        //Display Text

        Intent intent = getIntent();
        ///////////////

        tvCity = (TextView) findViewById(R.id.city_out); // Lab 4
        tvPostCode = (TextView) findViewById(R.id.post_out); // Lab 4
        tvRegion = (TextView) findViewById(R.id.region_out); // Lab 4

        //////////////

        //Display Star Sign Image
        //ivStarSign = (ImageView)findViewById(R.id.imgViewStarSign); // Lab 3

        //Intent iMainAct = getIntent(); // Lab 3

        //setup image
        /*String sImagePath = "drawable/" + starSignInfo.getStarSignImg(); // Lab 3
        Context appContext = getApplicationContext(); // Lab 3
        int imgResID = appContext.getResources().getIdentifier(sImagePath, "drawable", "com.example.graham.mondaysChild.app"); // Lab 3
        ivStarSign.setImageResource(imgResID); // Lab 3*/
    }

    public void onClick(View view) {

        setResult(Activity.RESULT_OK);
        finish();
    }
}
