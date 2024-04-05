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
 * ProfileCreationDetailThree activity is responsible for collecting user preferences on alcohol, smoking, food,
 * and marijuana usage as part of a profile creation or update process. This class manages user inputs from
 * dropdown menus (Spinners), converts these inputs to a standardized format, and updates the user's profile
 * in a database.
 */

public class ProfileCreationDetailThree extends AppCompatActivity {
    // Spinners for collecting user preferences
    private Spinner alcoholMenu, smokingMenu, foodMenu, marijuanaMenu;
    // Variables to store user preference selections
    String alcohol, smoking, food, marijuana;

    // Listener for device orientation changes
    private OrientationEventListener orientationEventListener;

    /**
     * Called when the activity is starting. This method is where the layout is set,
     * the UI components are initialized, and event listeners are set up.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetailthree);

        alcoholMenu = findViewById(R.id.alcohol_menu);
        smokingMenu = findViewById(R.id.smoking_menu);
        foodMenu = findViewById(R.id.Food_menu);
        marijuanaMenu = findViewById(R.id.Marijuana_menu);
        DatabaseHandler db = new DatabaseHandler();
        TextView Alcohol = findViewById(R.id.Alcohol);

        int unicodeAlcohol = 0x1F37A;

        String emojiAlcohol = getEmoji(unicodeAlcohol);

        String textAlcohol = "Alcohol" + emojiAlcohol;
        Alcohol.setText(textAlcohol);

        // Find the button by its ID
        // Setting up listener for the confirm button
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProfileCreationDetailThree
                db.updateUser(getData());
                Intent intent = new Intent(ProfileCreationDetailThree.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        // Orientation listener to handle screen orientation changes
        orientationEventListener = new OrientationEventListener(this) {
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
     * Gathers data from the UI components and prepares it for database update.
     * Converts user preferences from String to Boolean where applicable, and
     * compiles all the data into a Map object.
     *
     * @return A Map containing keys and values representing the user's preferences.
     */
    private Map<String, Object> getData() {
        // Get selections from each spinner
        String alcohol = alcoholMenu.getSelectedItem().toString();
        String smoking = smokingMenu.getSelectedItem().toString();
        String food = foodMenu.getSelectedItem().toString();
        String marijuana = marijuanaMenu.getSelectedItem().toString();
        // Convert string selections to Boolean where applicable
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
     * Converts a string input to a Boolean value. Specifically used for converting
     * user responses such as "Yes" to true and any other response to false.
     *
     * @param input The string input to convert.
     * @return The Boolean value of the input, null if input is null.
     */
    private Boolean convertToBoolean(String input) {
        // Check for null to avoid NullPointerException
        if (input == null) {
            return null;
        }
        // Return true if input is "Yes", false otherwise
        return input.equals("Yes");
    }

    /**
     * Converts a Unicode point to its corresponding emoji character.
     *
     * @param uni The Unicode point of the emoji.
     * @return A String containing the emoji character.
     */
    public String getEmoji(int uni){

        return new String(Character.toChars(uni));
    }

    /**
     * Disables the OrientationEventListener when the activity is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}