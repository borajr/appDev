package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SimpleChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);
        Button sendButton = findViewById(R.id.chat_send_button);

        //firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //messagesRef = firebaseDatabase.getReference("messages");

        //sendButton.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
              //  boolean correctInput = true;
           // }

      //  }

    }
}