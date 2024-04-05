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

public class ProfileCloneThree extends AppCompatActivity {
    private Spinner spinner_alcohol, spinner_smoking, spinner_diet, spinner_marijuana;
    private OrientationEventListener verticalOrient;

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
    private Boolean convertToBoolean(String input) {
        if (input == null) {
            return null;
        }
        return input.equals("Yes");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        verticalOrient.disable();
    }
}