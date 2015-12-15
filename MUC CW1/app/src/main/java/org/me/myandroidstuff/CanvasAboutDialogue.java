package org.me.myandroidstuff;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by Graham on 15/12/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CanvasAboutDialogue extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder mcAboutDialog = new AlertDialog.Builder(getActivity());
        mcAboutDialog.setMessage(R.string.Canvas_About)
                .setPositiveButton(R.string.dialog_About_OK_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        mcAboutDialog.setTitle("Canvas About");
        mcAboutDialog.setIcon(R.drawable.ic_menu_action_about);
        // Create the AlertDialog object and return it
        return mcAboutDialog.create();
    }
}
