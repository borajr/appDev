package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore; // Import for Firestore

public class SimpleChat extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat); // Assuming your layout file is named activity_simple_chat

        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        Button sendButton = findViewById(R.id.chat_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.chat_message_input);
                // Read the input field and push a new instance
                // of ChatMessage to the Firestore database
                db.collection("messages") // Assuming "messages" is your collection name
                        .add(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                // Clear the input
                input.setText("");
            }
        });
    }
}
