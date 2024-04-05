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

public class PreferenceTwo extends AppCompatActivity {

    private Spinner preferenceAlcohol, preferenceSmoke, preferenceDiet, preferenceWeed;
    String alcohol, smoking, food, marijuana;
    private OrientationEventListener verticalEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_detailtwo); // Ensure this matches your layout file

        View btnProfile = findViewById(R.id.confirm_button);
        DatabaseHandler db = new DatabaseHandler();
        // Set OnClickListener to the button

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
                Intent intent = new Intent(PreferenceTwo.this, MainPage.class);
                startActivity(intent);
            }
        });

        verticalEventListener = new OrientationEventListener(this) {
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
        verticalEventListener.enable();

    }


    private Map<String, Object> getData() {
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
        verticalEventListener.disable();
    }
}