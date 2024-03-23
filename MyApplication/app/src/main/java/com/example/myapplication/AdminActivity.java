package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Spinner spinnerReportedUsers;
    private TextView tvReportReason;
    private Button btnBanUser, btnRemoveFromList, btnSignOut;
    private List<String> reportedUsers;
    private List<String> reportReasons;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        spinnerReportedUsers = findViewById(R.id.spinnerReportedUsers);
        tvReportReason = findViewById(R.id.tvReportReason);
        btnBanUser = findViewById(R.id.btnBanUser);
        btnRemoveFromList = findViewById(R.id.btnRemoveFromList);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Initialize reported users and reasons
        reportedUsers = new ArrayList<>();
        reportReasons = new ArrayList<>();

        // Add dummy reported users and reasons
        reportedUsers.add("Arda Bulbul");
        reportedUsers.add("Efe Sarigul");
        reportReasons.add("Swag too good");
        reportReasons.add("Too Handsome");

        // Setup the spinner adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reportedUsers);
        spinnerReportedUsers.setAdapter(adapter);

        spinnerReportedUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Display the reason for the report
                tvReportReason.setText("User was reported for: " + reportReasons.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Clear the report reason text
                tvReportReason.setText("User was reported for: ");
            }
        });

        btnBanUser.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position < 0) {
                Toast.makeText(AdminActivity.this, "Please select a user", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(AdminActivity.this, reportedUsers.get(position) + " is banned", Toast.LENGTH_SHORT).show();
            // Remove the user from the list
            reportedUsers.remove(position);
            reportReasons.remove(position);
            adapter.notifyDataSetChanged();
        });

        btnRemoveFromList.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position < 0) {
                Toast.makeText(AdminActivity.this, "Please select a user", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(AdminActivity.this, reportedUsers.get(position) + " is removed from the list", Toast.LENGTH_SHORT).show();
            // Remove the user from the list
            reportedUsers.remove(position);
            reportReasons.remove(position);
            adapter.notifyDataSetChanged();
        });

        btnSignOut.setOnClickListener(v -> {
            // Sign out logic goes here
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
