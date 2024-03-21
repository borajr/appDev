package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat); // Replace with your chat activity layout file

        // Initialize the message list and adapter
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList);

        // Setup RecyclerView
        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Setup the menu button
        ImageView menuButton = findViewById(R.id.chat_menu_button); // Replace with your menu button ID
        menuButton.setOnClickListener(view -> {
            // Inflate the custom layout
            View popupView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_pop_up_menu, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
            alertDialogBuilder.setView(popupView);

            // Create the AlertDialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //BURA
            popupView.findViewById(R.id.button_unmatch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle "Unmatch" action
                    alertDialog.dismiss();
                }
            });

            popupView.findViewById(R.id.button_report).setOnClickListener(v -> {
                // Handle "Report" action
                alertDialog.dismiss();
            });

            // Show the AlertDialog
            alertDialog.show();
            });
    }

    // ... Rest of your ChatActivity code
}
