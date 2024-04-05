package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PopupUtils extends AppCompatActivity {

    public static void showMenuPopup(Activity activity, Runnable onUnmatch, Runnable onReport) {
        View popupView = LayoutInflater.from(activity).inflate(R.layout.chat_pop_up_menu, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(popupView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        popupView.findViewById(R.id.button_unmatch).setOnClickListener(v -> {
            onUnmatch.run();
            alertDialog.dismiss();
        });

        popupView.findViewById(R.id.button_report).setOnClickListener(v -> {
            alertDialog.dismiss();
            onReport.run();
        });

        alertDialog.show();
    }
}