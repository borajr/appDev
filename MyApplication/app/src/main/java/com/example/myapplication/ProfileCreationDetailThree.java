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

public class ProfileCreationDetailThree extends AppCompatActivity {
    private Spinner alcoholMenu, smokingMenu, foodMenu, marijuanaMenu;
    String alcohol, smoking, food, marijuana;

    private OrientationEventListener orientationEventListener;

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
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProfileCreationDetailThree
                db.updateUser(getData());
                Intent intent = new Intent(ProfileCreationDetailThree.this, CameraActivity.class);
                startActivity(intent);
            }
        });

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

    private Map<String, Object> getData() {
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
    private Boolean convertToBoolean(String input) {
        if (input == null) {
            return null;
        }
        return input.equals("Yes");
    }

    public String getEmoji(int uni){

        return new String(Character.toChars(uni));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}
