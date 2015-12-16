package org.me.myandroidstuff;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Graham on 15/12/2015.
 */
public class CanvasActivity extends Activity{

    FragmentManager fmAboutDialogue;                // Lab 2 - Fragment Manager
    // ****************************************************************
    // onCreate.
    // An event handler called when the app is first created.
    // Usually contains all initializations and setting up for the app.
    // Generally the place to show the app main UI screen.
    // ****************************************************************
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_canvas_screen); // app main UI screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new mySurfaceView(this));
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
}
