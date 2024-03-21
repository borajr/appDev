package com.example.myapplication; // Replace with your actual package name

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileCloneOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML file name for ProfileCloneOne
        setContentView(R.layout.activity_profileoneclone);

        // Initialize your UI components here, assuming you use similar IDs
        EditText firstNameEditText = findViewById(R.id.editText1);
        EditText lastNameEditText = findViewById(R.id.editText2);
        DatePicker datePicker = findViewById(R.id.datePicker);
        Button confirmButton = findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                // Check if any field is empty
                if(firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(ProfileCloneOne.this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Age check
                if (!isOldEnough()) {
                    Toast.makeText(ProfileCloneOne.this, "You must be at least 18 years old.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Navigate to the next page
                Intent intent = new Intent(ProfileCloneOne.this, ProfileSetup.class); // Adjust as necessary
                startActivity(intent);
            }
        });
    }

    private boolean isOldEnough() {
        DatePicker datePicker = findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -18);

        return !selectedDate.after(today); // True if 18 or older
    }
}
