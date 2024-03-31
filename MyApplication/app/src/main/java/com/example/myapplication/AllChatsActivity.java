package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity implements ChatAdapter.ChatClickListener {
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
        adapter = new ChatAdapter(chatList, this);
        recyclerView.setAdapter(adapter);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_chats);

        // Set OnClickListener to the button
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(AllChatsActivity.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    startActivity(new Intent(AllChatsActivity.this, MainPage.class));
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(AllChatsActivity.this, AllChatsActivity.class));
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onChatClick(chat chatData) {
        // Handle the click event, e.g., start a new activity with details of the clicked chat
        Intent intent = new Intent(AllChatsActivity.this, SimpleChat.class);
        startActivity(intent);
    }
}
