package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ProfileCreationDetailTwo extends AppCompatActivity {

    private Spinner genderSpinner, heightSpinner, starSignSpinner;
    private EditText academicProgram, languageText;
    private Button confirmButton;
    private String gender, starSign, language, major;
    private Integer height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetailtwo); // Ensure this matches your layout file

        // Initialize your views
        genderSpinner = findViewById(R.id.editText1);
        heightSpinner = findViewById(R.id.minHeightSpinner);
        //minAgeSpinner = findViewById(R.id.minAgeSpinner);
        confirmButton = findViewById(R.id.confirm_button);
        starSignSpinner = findViewById(R.id.starSignSpinner);
        academicProgram = findViewById(R.id.editText5);
        languageText = findViewById(R.id.edittext7);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the next appropriate page
                DatabaseHandler db = new DatabaseHandler();
                db.updateUser(getData());
                Intent nextIntent = new Intent(ProfileCreationDetailTwo.this, ProfileCreationDetailThree.class); // Adjust as needed
                finish();
                startActivity(nextIntent);

            }
        });
    }

//    private boolean validatePreferences() {
//        int minHeightIndex = heightSpinner.getSelectedItemPosition();
//        int maxHeightIndex = maxHeightSpinner.getSelectedItemPosition();
//        if (minHeightIndex > maxHeightIndex) {
//            Toast.makeText(this, "Minimum height cannot be greater than maximum height", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        // Validate ages
//        int minAgeIndex = minAgeSpinner.getSelectedItemPosition();
//        int maxAgeIndex = maxAgeSpinner.getSelectedItemPosition();
//        if (minAgeIndex > maxAgeIndex) {
//            Toast.makeText(this, "Minimum age cannot be greater than maximum age", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true; // All validations passed
//    }

    private Map<String, Object> getData() {
        String gender = genderSpinner.getSelectedItem().toString();
        String heightS = heightSpinner.getSelectedItem().toString();

        int height;

        if (heightS.startsWith("<")) {
            height = Integer.parseInt(heightS.substring(1)) - 1;
        } else if (heightS.startsWith(">")) {
            height = Integer.parseInt(heightS.substring(1)) + 1;
        } else {
            height = Integer.parseInt(heightS);
        }

        String starSign = starSignSpinner.getSelectedItem().toString();
        String major = academicProgram.getText().toString().trim();
        String language = languageText.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();
        map.put("gender", gender);
        map.put("height", height);
        map.put("starSign", starSign);
        map.put("academicProgram", major);
        map.put("language", language);
        return map;
    }

}
