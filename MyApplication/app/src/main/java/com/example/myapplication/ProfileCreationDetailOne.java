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

    private OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your layout
        setContentView(R.layout.activity_profiledetailone); // Ensure this matches your XML file name

        // Initialize your UI components here
        EditText firstNameEditText = findViewById(R.id.editText1);
        EditText lastNameEditText = findViewById(R.id.editText2);
        DatePicker datePicker = findViewById(R.id.datePicker); // If you plan to use the DatePicker value
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you have EditText fields for first name, last name, etc.
                EditText firstNameEditText = findViewById(R.id.editText1);
                EditText lastNameEditText = findViewById(R.id.editText2);
                // Continue for other EditTexts as necessary

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                // Continue for other fields as necessary

                // Check if any field is empty
                if(firstName.isEmpty() || lastName.isEmpty()) { // Extend this condition for all fields
                    Toast.makeText(ProfileCreationDetailOne.this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Age check - Reuse isOldEnough() method from previous example
                if (!isOldEnough()) {
                    Toast.makeText(ProfileCreationDetailOne.this, "You must be at least 18 years old.", Toast.LENGTH_LONG).show();
                    return;
                }
                DatabaseHandler db = new DatabaseHandler();
                int year = datePicker.getYear();
                Calendar today = Calendar.getInstance(); //CREATE HASHMAP HERE, DB.UPDATEUSER SHOULD JUST UPLOAD THE HASHMAP!!
                Map<String, Object> data = createMap(firstName, lastName, 2024-year);
                db.updateUser(data);
                // If all validations pass, navigate to the next page
                Intent intent = new Intent(ProfileCreationDetailOne.this, ProfileCreationDetailTwo.class);
                finish();
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

        // Further initialization can be done as needed
    }

    private boolean isOldEnough() {
        DatePicker datePicker = findViewById(R.id.datePicker); // Make sure the ID matches
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -18);

        return !selectedDate.after(today); // True if 18 or older
    }

    private Map<String, Object> createMap(String name, String lastName, Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("lastName", lastName);
        map.put("age", age);
        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}
