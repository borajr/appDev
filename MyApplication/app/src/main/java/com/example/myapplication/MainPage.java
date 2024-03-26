package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean matched = true;

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Set the OnNavigationItemSelectedListener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(MainPage.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    // You can update the UI to indicate this is the current page
                    // or perform other actions appropriate for clicking "Main"
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(MainPage.this, AllChatsActivity.class));
                    return true;
                }

                return false;
            }
        });

        if (matched) {
            showMatchPopup();
        }

    }

    private void showMatchPopup() {
        // Inflate the match_popup.xml layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.match_popup, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(popupView);

        // Create and show the dialog
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        // Set random values for location and time from arrays (assuming these arrays exist)
        String[] locations = {"Luna Ground Floor",
        	    "Hubble",
        	    "Neuron Cafe",
        	    "Neuron Upstairs Common Area",
        	    "Traverse Entrance",
        	    "Atlas Food City",
        	    "Metaforum Food City",
        	    "Gemini Food City",
                "Flux Food City",
                "Metaforum Ground Floor",
        	    "Auditorium In Front of Subway"};
        String[] times = {"Lunch Time Wednesday",
                	    "Lunch Time Monday",
                	    "Lunch Time Tuesday",
                	    "Lunch Time Thursday",
                	    "Lunch Time Friday",
                	    "18.00 Wednesday",
                	    "18.00 Monday",
                        "18.00 Tuesday",
                        "18.00 Thursday",
                        "18.00 Friday" };
        Random random = new Random();
        String randomLocation = locations[random.nextInt(locations.length)];
        String randomTime = times[random.nextInt(times.length)];

        // Find the TextViews and set the location and time

        TextView locationTextView = popupView.findViewById(R.id.textViewMatchLocation);
        locationTextView.setText("Location: " + randomLocation);
        TextView timeTextView = popupView.findViewById(R.id.textViewMatchTime);
        timeTextView.setText("Time: " + randomTime);

    }
}

