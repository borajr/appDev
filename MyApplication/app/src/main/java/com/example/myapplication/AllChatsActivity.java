package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        // Initialize the chat list
        chatList = new ArrayList<>();
        // Add dummy data to the list
        chatList.add(new chat("Birol", "Genel evleri kapatalım da millet bizi mi...", "4:30 PM", R.drawable.ic_profile_placeholder));
        chatList.add(new chat("Taylan", "Koyayım da yaylan.", "4:30 PM", R.drawable.ic_profile_placeholder));
        // Add more dummy data as needed

        recyclerView = findViewById(R.id.recyclerView_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);

        View btnChat = findViewById(R.id.button_chat);
        View btnMain = findViewById(R.id.button_main);
        View btnProfile = findViewById(R.id.button_profile);


        // Set OnClickListener to the button
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(AllChatsActivity.this, MainPage.class);
                startActivity(intent); // Start the new activity
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(AllChatsActivity.this, ProfileSetup.class);
                startActivity(intent); // Start the new activity
            }
        });
    }


}
