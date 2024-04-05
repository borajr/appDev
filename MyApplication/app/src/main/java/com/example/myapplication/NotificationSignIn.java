package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


/**
 * NotificationSignIn activity is responsible for displaying a sign-in notification screen to the user,
 * with an option to navigate to the starting page of the application either after a delay or immediately upon user action.
 */
public class NotificationSignIn extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final Runnable navigateToStartingPage = new Runnable() {
        @Override
        public void run() {
            goToStartingPage();
        }
    };

    /**
     * Initializes the activity, sets the content view, and schedules navigation to the Starting Page.
     * Also provides a mechanism for immediate navigation if the user chooses.
     *
     * @param savedInstanceState Contains data supplied in onSaveInstanceState(Bundle) if the activity is re-initialized.
     */
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

    /**
     * Initiates the transition to the Starting Page of the application and finishes the current activity.
     */
    private void goToStartingPage() {
        Intent intent = new Intent(NotificationSignIn.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish this Activity so the user can't navigate back to it with the back button
    }

    /**
     * Ensures that any scheduled navigation is canceled when the activity is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure that the handler callbacks are removed when the activity is destroyed
        handler.removeCallbacks(navigateToStartingPage);
    }
}