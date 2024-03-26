package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

}
