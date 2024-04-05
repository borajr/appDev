package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * PreferenceTwo is an activity that collects user preferences related to lifestyle choices such as alcohol,
 * smoking, diet, and marijuana usage. It updates these preferences in the user's profile in the database.
 */
public class PreferenceTwo extends AppCompatActivity {

    // Spinners for collecting user preferences
    private Spinner preferenceAlcohol, preferenceSmoke, preferenceDiet, preferenceWeed;
    // Variables to hold the selections from the spinners
    String alcohol, smoking, food, marijuana;

    // Listener for handling orientation changes
    private OrientationEventListener verticalEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_detailtwo); // Sets the layout for this activity

        View btnProfile = findViewById(R.id.confirm_button);
        // Initialize database handler
        DatabaseHandler db = new DatabaseHandler();
        // Set OnClickListener to the button

        // Initialize spinners by finding them by ID
        preferenceAlcohol = findViewById(R.id.alcohol_menu);
        preferenceSmoke = findViewById(R.id.smoking_menu);
        preferenceDiet = findViewById(R.id.Food_menu);
        preferenceWeed = findViewById(R.id.Marijuana_menu);

        // Find the button by its ID
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProfileCreationDetailThree
                db.updateUserPref(getData());
                // Navigate to MainPage activity
                Intent intent = new Intent(PreferenceTwo.this, MainPage.class);
                startActivity(intent);
            }
        });

        // Initialize and enable orientation event listener for adjusting screen orientation
        verticalEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                // Adjust screen orientation based on device orientation
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
        verticalEventListener.enable();

    }


    /**
     * Collects user preferences from spinners and prepares them for database storage.
     *
     * @return A map containing the keys and values representing the user's lifestyle choices.
     */
    private Map<String, Object> getData() {
        // Retrieve selections from each spinner
        String alcohol = preferenceAlcohol.getSelectedItem().toString();
        String smoking = preferenceSmoke.getSelectedItem().toString();
        String food = preferenceDiet.getSelectedItem().toString();
        String marijuana = preferenceWeed.getSelectedItem().toString();
        Boolean alcoholBool = convertToBoolean(alcohol);
        Boolean smokingBool = convertToBoolean(smoking);
        Boolean marijuanaBool = convertToBoolean(marijuana);


        // Call createMap() with the collected data
        Map<String, Object> unhealthyMap = new HashMap<>();
        unhealthyMap.put("alcohol", alcoholBool);
        unhealthyMap.put("smoking", smokingBool);
        unhealthyMap.put("food", food);
        unhealthyMap.put("marijuana", marijuanaBool);
        return unhealthyMap;
    }

    /**
     * Converts a string input to a Boolean value, specifically converting "Yes" to true.
     *
     * @param input The user input string.
     * @return The Boolean value of the input, null if input is null.
     */
    private Boolean convertToBoolean(String input) {
        if (input == null) {
            return null;
        }
        return input.equals("Yes");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disables the orientation event listener to prevent memory leaks
        verticalEventListener.disable();
    }
}