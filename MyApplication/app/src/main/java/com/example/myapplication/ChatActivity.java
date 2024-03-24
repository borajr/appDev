package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_simplechat);

        // Initialize the message list and adapter
        messageList = new ArrayList<>();
        // Example message
        messageList.add(new ChatMessage("Hi!", true, null, 0));
        messageList.add(new ChatMessage("Hello!", false, null, 0));

        chatAdapter = new SingleChatAdapter(messageList);

        // Setup RecyclerView
        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Setup the menu button
        ImageView menuButton = findViewById(R.id.chat_menu_button);
        menuButton.setOnClickListener(view -> showMenuPopup());
    }

    private void showMenuPopup() {
        View popupView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_pop_up_menu, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        alertDialogBuilder.setView(popupView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        // Handle "Unmatch" button click
        popupView.findViewById(R.id.button_unmatch).setOnClickListener(v -> {
            // Add logic to handle unmatching here, if needed
            transitionToMainActivity();
            alertDialog.dismiss();
        });

        // Handle "Report" button click
        popupView.findViewById(R.id.button_report).setOnClickListener(v -> {
            alertDialog.dismiss();
            showReportPopup();
        });

        alertDialog.show();
    }

    private void showReportPopup() {
        View reportPopupView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.report_pop_up, null);
        AlertDialog.Builder reportDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        reportDialogBuilder.setView(reportPopupView);
        AlertDialog reportDialog = reportDialogBuilder.create();
        reportDialog.setCanceledOnTouchOutside(true);
        EditText reasonEditText = reportPopupView.findViewById(R.id.edittext_report_reason); // Ensure this ID matches your layout
        reportPopupView.findViewById(R.id.button_send_report).setOnClickListener(v -> {
            // Here you can handle sending the report reason to your server or save it
            String reportReason = reasonEditText.getText().toString().trim();
            if (!reportReason.isEmpty()) {
                // TODO: Send report reason to your backend/server
                Toast.makeText(ChatActivity.this, "Report sent for: " + reportReason, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChatActivity.this, "Please enter a reason for reporting.", Toast.LENGTH_SHORT).show();
            }
            reportDialog.dismiss();
            transitionToMainActivity(); // Transition to the main activity after reporting
        });

        reportDialog.show();
    }

    private void transitionToMainActivity() {
        // Transition to the main activity
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
