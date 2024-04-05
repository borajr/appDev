package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Utility class for displaying pop-up menus in activities.
 */
public class PopupUtils extends AppCompatActivity {

    /**
     * Displays a menu popup with options to unmatch or report a user.
     *
     * @param activity The current activity context in which the popup will be displayed.
     * @param onUnmatch A runnable to execute when the unmatch option is selected.
     * @param onReport A runnable to execute when the report option is selected.
     */
    public static void showMenuPopup(Activity activity, Runnable onUnmatch, Runnable onReport) {
        // Inflate the custom layout for the popup.
        View popupView = LayoutInflater.from(activity).inflate(R.layout.chat_pop_up_menu, null);
        // Build the alert dialog to host the popup view.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(popupView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        // Set the onClickListener for the "Unmatch" button.
        popupView.findViewById(R.id.button_unmatch).setOnClickListener(v -> {
            onUnmatch.run();
            alertDialog.dismiss();
        });

        // Set the onClickListener for the "Report" button.
        popupView.findViewById(R.id.button_report).setOnClickListener(v -> {
            alertDialog.dismiss();
            onReport.run();
        });

        // Display the popup.
        alertDialog.show();
    }
}