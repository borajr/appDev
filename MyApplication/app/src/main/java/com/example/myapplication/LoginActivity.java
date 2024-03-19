package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the "Forgot Password?" text view and set an OnClickListener on it
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start TroubleSigningInActivity
                Intent intent = new Intent(LoginActivity.this, TroubleSigningInActivity.class);
                startActivity(intent);
            }
        });
    }
}
