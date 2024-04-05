package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * PreferenceCloneTwo is an activity for collecting user preferences regarding alcohol, smoking, food, and marijuana usage.
 * It updates these preferences in the database and supports dynamic orientation changes.
 */
public class PreferenceCloneTwo extends AppCompatActivity {

    // UI components for collecting user preferences
    private Spinner alcoholMenu, smokingMenu, foodMenu, marijuanaMenu;

    // Variables to store user selections
    String alcohol, smoking, food, marijuana;

    // Listener for handling orientation changes dynamically
    private OrientationEventListener orientationEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_clonetwo); // Ensure this matches your layout file

        View btnProfile = findViewById(R.id.confirm_button);
        DatabaseHandler db = new DatabaseHandler();
        // Set OnClickListener to the button

        // Initialize UI components
        alcoholMenu = findViewById(R.id.alcohol_menu);
        smokingMenu = findViewById(R.id.smoking_menu);
        foodMenu = findViewById(R.id.Food_menu);
        marijuanaMenu = findViewById(R.id.Marijuana_menu);
        TextView Alcohol = findViewById(R.id.Alcohol);

        int unicodeAlcohol = 0x1F37A; // Unicode for the alcohol beverage glass emoji

        String emojiAlcohol = getEmoji(unicodeAlcohol); // Converts Unicode to String
        String textAlcohol = "Alcohol" + emojiAlcohol; // Concatenates label with emoji

        Alcohol.setText(textAlcohol);

        // Find the button by its ID
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProfileCreationDetailThree
                db.updateUserPref(getData());
                Intent intent = new Intent(PreferenceCloneTwo.this, ProfileSetup.class);
                startActivity(intent);
            }
        });

        // Initializes and enables the OrientationEventListener
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) { // Adjusts screen orientation based on device orientation
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
     * Collects user preferences from the UI and prepares them for database storage.
     * @return A map containing the keys and values of user preferences.
     */
    private Map<String, Object> getData() {
        // Retrieves user preferences from each spinner
        String alcohol = alcoholMenu.getSelectedItem().toString();
        String smoking = smokingMenu.getSelectedItem().toString();
        String food = foodMenu.getSelectedItem().toString();
        String marijuana = marijuanaMenu.getSelectedItem().toString();
        Boolean alcoholBoolean = convertToBoolean(alcohol);
        Boolean smokingBoolean = convertToBoolean(smoking);
        Boolean marijuanaBoolean = convertToBoolean(marijuana);


        // Call createMap() with the collected data
        Map<String, Object> map = new HashMap<>();
        map.put("alcohol", alcoholBoolean);
        map.put("smoking", smokingBoolean);
        map.put("food", food);
        map.put("marijuana", marijuanaBoolean);
        return map;
    }

    /**
     * Converts a given Unicode point into its corresponding emoji character.
     * @param uni The Unicode point of the emoji.
     * @return A String containing the emoji character.
     */
    public String getEmoji(int uni){

        return new String(Character.toChars(uni)); // Conversion of Unicode to String
    }

    /**
     * Converts a string input to a Boolean value, specifically "Yes" to true.
     * @param input The user input string.
     * @return The Boolean value of the input, null if input is null.
     */
    private Boolean convertToBoolean(String input) {
        if (input == null) {
            return null;
        }
        return input.equals("Yes"); // Returns true if input is "Yes"
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        // Prevents memory leaks by disabling the orientation listener
        orientationEventListener.disable();
    }
}