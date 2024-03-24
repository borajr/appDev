package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationSignIn extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final Runnable navigateToStartingPage = new Runnable() {
        @Override
        public void run() {
            goToStartingPage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_signin);

        // Schedule the transition to the Starting Page after 45 seconds
        handler.postDelayed(navigateToStartingPage, 15000);

        // Assuming you have a button in your layout to allow the user to skip the wait
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked the button, remove the delayed navigation
                handler.removeCallbacks(navigateToStartingPage);
                // Go to the Starting Page immediately
                goToStartingPage();
            }
        });
    }

    private void goToStartingPage() {
        Intent intent = new Intent(NotificationSignIn.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish this Activity so the user can't navigate back to it with the back button
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure that the handler callbacks are removed when the activity is destroyed
        handler.removeCallbacks(navigateToStartingPage);
    }
}
