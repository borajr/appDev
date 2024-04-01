package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountInfo extends AppCompatActivity {

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Find the buttons or text views
        View btnMain = findViewById(R.id.btnmain);
        View btnSignOut = findViewById(R.id.btnSignOut);
        View btnHadiSiktirGit = findViewById(R.id.btnDelete);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(AccountInfo.this, MainPage.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnHadiSiktirGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete account
                Toast.makeText(AccountInfo.this, "Your account will be deleted in 10 business days. Hope to see not see you",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountInfo.this, MainPage.class);
                startActivity(intent);
            }
        });
    }
}
