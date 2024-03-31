package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SimpleChat extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore instance
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private RecyclerView recyclerViewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view); // Make sure you have this RecyclerView in your layout
        chatAdapter = new SingleChatAdapter(messageList);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        Button sendButton = findViewById(R.id.chat_send_button);
        sendButton.setOnClickListener(view -> {
            EditText input = findViewById(R.id.chat_message_input);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // If user is signed in
                ChatMessage newMessage = new ChatMessage(input.getText().toString(), true, null, 0); // Adjust according to your ChatMessage constructor
                db.collection("messages").add(newMessage)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "Message sent successfully"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error sending message", e));
            } else {
                // Handle the case where there is no user signed in
                Log.d(TAG, "User is not signed in");
                // Optionally, you can prompt the user to sign in
            }

            input.setText(""); // Clear the input field
        });

        listenForMessages();
    }

    private void listenForMessages() {
        db.collection("messages")
                .orderBy("timestamp") // Assuming there is a timestamp field in your ChatMessage
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    List<ChatMessage> fetchedMessages = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        ChatMessage message = doc.toObject(ChatMessage.class);
                        fetchedMessages.add(message);
                    }

                    messageList.clear();
                    messageList.addAll(fetchedMessages);
                    chatAdapter.notifyDataSetChanged();
                });
    }
}
