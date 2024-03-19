package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class TroubleSigningInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble_signing_in);

        final EditText emailEditText = findViewById(R.id.editText1);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = emailEditText.getText().toString();
                if (emailInput.contains("tue.nl")) {
                    Intent intent = new Intent(TroubleSigningInActivity.this, NotificationSignIn.class);
                    startActivity(intent);
                }
            }
        });
    }
}
