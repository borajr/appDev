package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_page);

        // Find the button by its ID
        View btnChangePass = findViewById(R.id.changepass);
        View btnYouBasics = findViewById(R.id.basics);
        View btnYouInfo = findViewById(R.id.info);
        View btnYouLifestyle = findViewById(R.id.lifestyle);
        View btnThemInfo = findViewById(R.id.info2);
        View btnThemLifestyle2 = findViewById(R.id.lifestyle2);
        View btnChangePhotos = findViewById(R.id.changephoto);
        View btnAccountInfo = findViewById(R.id.goodbye);

        // Set OnClickListener to the button
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ChangePassword.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouBasics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneOne.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouLifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneThree.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnThemLifestyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, PreferenceCloneOne.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnThemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, PreferenceCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        btnChangePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ChangePhotoActivity.class);
                startActivity(intent); // Start the new activity
            }
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);

        // Set OnClickListener to the button
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(ProfileSetup.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    startActivity(new Intent(ProfileSetup.this, MainPage.class));
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(ProfileSetup.this, AllChatsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}

