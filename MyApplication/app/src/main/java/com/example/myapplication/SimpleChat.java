package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore; // Import for Firestore

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleChat extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore instance
    private FirebaseAuth mAuth;
    private final String TAG = "SimpleChat";

    // Assuming you have a RecyclerView named recyclerView
    RecyclerView recyclerView = findViewById(R.id.chat_messages_recycler_view);
    List<String> messages = new ArrayList<>(); // This should be populated with messages from Firestore

    // Inside onSuccess method of Firestore write operation, update the RecyclerView



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat); // Assuming your layout file is named activity_simple_chat

        db = FirebaseFirestore.getInstance(); // Initialize Firestore


        Button sendButton = findViewById(R.id.chat_send_button);
        EditText input = findViewById(R.id.chat_message_input);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.chat_message_input);

                // Create a timestamp of the current date and time
                Timestamp timestamp = Timestamp.now();

                Map<String, Object> message = new HashMap<>();
                message.put("content", input.getText().toString()); // Assuming you want to put the text content of the input field
                message.put("timestamp", timestamp);

                // Read the input field and push a new instance
                // of ChatMessage to the Firestore database
                db.collection("messages") // Assuming "messages" is your collection name
                        .document()
                        .set(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document successfully written
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                messages.add(input.getText().toString());

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error handling
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                // Clear the input
                input.setText("");
            }
        }); // <-- Add closing parenthesis for setOnClickListener method
    }
}


