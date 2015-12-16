package org.me.myandroidstuff;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Graham on 16/12/2015.
 */
public class SavedPrefActivity extends Activity implements Serializable {

    // *********************************************
    // Declare variables etc.
    // *********************************************
    SharedPreferences sharedPrefs;
    private String strCity;
    private String strPostCode;
    private String strRegion;

    // *********************************************
    // Declaring Getters and Setters
    // *********************************************
    private void setCity(String str) { this.strCity = str; }
    public String getCity() { return strCity; }
    private void setPostCode(String str)
    {
        this.strPostCode = str;
    }
    public String getPostCode()
    {
        return strPostCode;
    }
    private void setRegion(String str)
    {
        this.strRegion = str;
    }
    public String getRegion()
    {
        return strRegion;
    }

    // **************************************************
    // Declare constructor and date manipulation methods.
    // **************************************************
    public SavedPrefActivity(SharedPreferences mcSDPrefs){

        setCity("Stirling");
        setPostCode("FK95SE");
        setRegion("StirlingShire");
        try {
            this.sharedPrefs = mcSDPrefs;
        }
        catch (Exception e) {
            Log.e("n", "Pref Manager is NULL");
        }

        setDefaultPrefs();
    }

    public void savePreferences(String key, boolean value) {

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferences(String key, String value) {

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setDefaultPrefs() {

        savePreferences("City", "Stirling");
        savePreferences("PostCode", "FK95SE");
        savePreferences("Region", "Stirlingshire");
    }
}
