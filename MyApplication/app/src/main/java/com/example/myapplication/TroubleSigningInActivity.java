package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code TroubleSigningInActivity} class extends {@link AppCompatActivity} and provides
 * a UI for users having trouble signing into the application. It includes functionality to assist
 * users based on their input and adjusts the screen orientation based on the device's orientation.
 */
public class TroubleSigningInActivity extends AppCompatActivity {

    private OrientationEventListener orientationEventListener;

    /**
     * Initializes the activity. This includes setting up the UI layout, configuring
     * the orientation event listener, and setting up a button click listener to handle user input.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble_signing_in);

        final EditText emailEditText = findViewById(R.id.editText1);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            /**
             * Called when the sign-in button is clicked. This method checks the user's email input
             * and proceeds to the next activity if it meets certain conditions.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                String emailInput = emailEditText.getText().toString();
                if (emailInput.contains("tue.nl")) {
                    Intent intent = new Intent(TroubleSigningInActivity.this, NotificationSignIn.class);
                    startActivity(intent);
                }
            }
        });
        orientationEventListener = new OrientationEventListener(this) {
            /**
             * Responds to changes in device orientation. Adjusts the activity's screen orientation
             * based on the device's current orientation.
             *
             * @param orientation The new orientation of the device.
             */
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 45 && orientation < 135) {
                    // Landscape mode, set screen orientation to reverse portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation >= 135 && orientation < 225) {
                    // Upside down mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else if (orientation >= 225 && orientation < 315) {
                    // Reverse landscape mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else {
                    // Portrait mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        };

        // Start the OrientationEventListener
        orientationEventListener.enable();
    }

    /**
     * Cleans up resources used by the activity, specifically disables the orientation event listener
     * to prevent memory leaks when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}