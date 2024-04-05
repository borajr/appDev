package com.example.myapplication; // Replace with your actual package name

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ProfileCreationDetailOne extends AppCompatActivity {

    private OrientationEventListener orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetailone);

        // Initialize your UI components here
        DatePicker dateSelect = findViewById(R.id.datePicker);
        Button btnConfirm = findViewById(R.id.confirm_button);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you have EditText fields for first name, last name, etc.
                EditText firstNameEditText = findViewById(R.id.editText1);
                EditText lastNameEditText = findViewById(R.id.editText2);
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                // Continue for other fields as necessary

                // Check if any field is empty
                if(firstName.isEmpty() || lastName.isEmpty()) { // Extend this condition for all fields
                    Toast.makeText(ProfileCreationDetailOne.this, "Please fill in all the fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Age check - Reuse isOldEnough() method from previous example
                if (!isOldEnough()) {
                    Toast.makeText(ProfileCreationDetailOne.this, "You must be 18 years old, or older.", Toast.LENGTH_LONG).show();
                    return;
                }
                DatabaseHandler db = new DatabaseHandler();
                int year = dateSelect.getYear();
                Calendar today = Calendar.getInstance();
                Map<String, Object> data = createMap(firstName, lastName, 2024-year);
                db.updateUser(data);
                // If all validations pass, navigate to the next page
                Intent intent = new Intent(ProfileCreationDetailOne.this, ProfileCreationDetailTwo.class);
                finish();
                startActivity(intent);
            }
        });

        orientation = new OrientationEventListener(this) {
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
        orientation.enable();

        // Further initialization can be done as needed
    }

    private boolean isOldEnough() {
        DatePicker dateSelector = findViewById(R.id.datePicker); // Make sure the ID matches
        int day = dateSelector.getDayOfMonth();
        int month = dateSelector.getMonth();
        int year = dateSelector.getYear();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -18);

        return !selectedDate.after(today); // True if 18 or older
    }

    private Map<String, Object> createMap(String name, String lastName, Integer age) {
        Map<String, Object> mapDetail = new HashMap<>();
        mapDetail.put("name", name);
        mapDetail.put("lastName", lastName);
        mapDetail.put("age", age);
        return mapDetail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientation.disable();
    }
}