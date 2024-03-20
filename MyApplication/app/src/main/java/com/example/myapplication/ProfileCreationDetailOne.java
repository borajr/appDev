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

public class ProfileCreationDetailOne extends AppCompatActivity {

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

                // If all validations pass, navigate to the next page
                Intent intent = new Intent(ProfileCreationDetailOne.this, ProfileCreationDetailTwo.class);
                startActivity(intent);
            }
        });


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

}
