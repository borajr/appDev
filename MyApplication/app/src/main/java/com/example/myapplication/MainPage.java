package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button by its ID
        View btnChat = findViewById(R.id.button_chat);
        View btnMain = findViewById(R.id.button_main);
        View btnProfile = findViewById(R.id.button_profile);


        // Set OnClickListener to the button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(MainPage.this, ProfileSetup.class);
                startActivity(intent); // Start the new activity
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(MainPage.this, AllChatsActivity.class);
                startActivity(intent); // Start the new activity
            }
        });
    }
}