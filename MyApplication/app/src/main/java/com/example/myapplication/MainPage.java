package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    startActivity(new Intent(MainPage.this, ChatActivity.class));
                    return true;
                }

                return false;
            }
        });

    }
}
