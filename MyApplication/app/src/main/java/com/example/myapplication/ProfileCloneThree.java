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
 * Manages the third step of the profile creation or update process, capturing preferences related to lifestyle choices.
 */
public class ProfileCloneThree extends AppCompatActivity {
    private Spinner spinner_alcohol, spinner_smoking, spinner_diet, spinner_marijuana;
    private OrientationEventListener verticalOrient;

    /**
     * Initializes the activity, its views, and the orientation event listener.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetailthree);

        spinner_alcohol = findViewById(R.id.alcohol_menu);
        spinner_smoking = findViewById(R.id.smoking_menu);
        spinner_diet = findViewById(R.id.Food_menu);
        spinner_marijuana = findViewById(R.id.Marijuana_menu);
        DatabaseHandler db = new DatabaseHandler();

        // Find the button by its ID
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProfileCreationDetailThree
                db.updateUser(getData());
                Intent intent = new Intent(ProfileCloneThree.this, ProfileSetup.class);
                startActivity(intent);
            }
        });

        verticalOrient = new OrientationEventListener(this) {
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
        verticalOrient.enable();
    }

    /**
     * Collects user preferences from spinners and constructs a map with the data.
     *
     * @return A map containing user preferences.
     */
    private Map<String, Object> getData() {
        String alcohol = spinner_alcohol.getSelectedItem().toString();
        String smoking = spinner_smoking.getSelectedItem().toString();
        String food = spinner_diet.getSelectedItem().toString();
        String marijuana = spinner_marijuana.getSelectedItem().toString();
        Boolean alcoholBoolean = convertToBoolean(alcohol);
        Boolean smokingBoolean = convertToBoolean(smoking);
        Boolean marijuanaBoolean = convertToBoolean(marijuana);


        // Call createMap() with the collected data
        Map<String, Object> PreferenceMap = new HashMap<>();
        PreferenceMap.put("alcohol", alcoholBoolean);
        PreferenceMap.put("smoking", smokingBoolean);
        PreferenceMap.put("food", food);
        PreferenceMap.put("marijuana", marijuanaBoolean);
        return PreferenceMap;
    }

    /**
     * Converts a string representation ("Yes" or "No") to a Boolean value.
     *
     * @param input The string input to convert.
     * @return True if the input is "Yes", otherwise false.
     */
    private Boolean convertToBoolean(String input) {
        if (input == null) {
            return null;
        }
        return input.equals("Yes");
    }

    /**
     * Disables the OrientationEventListener when the activity is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        verticalOrient.disable();
    }
}