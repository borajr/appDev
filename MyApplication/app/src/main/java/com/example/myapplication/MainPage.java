package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    int i = 0;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference usersReference = db.collection("users");
    ListView listView;
    List<cards> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean matched = true;

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Set the OnNavigationItemSelectedListener
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(MainPage.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    // You can update the UI to indicate this is the current page
                    // or perform other actions appropriate for clicking "Main"
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(MainPage.this, AllChatsActivity.class));
                    return true;
                }

                return false;
            }
        });

        Button infoButton = findViewById(R.id.infoButton);

        compareGendersWithAllUsers();
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInfo();
            }
        });


        if (matched) {
            //moreInfo();
            showMatchPopup();
        }

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                // Do something on the left!
                // You also have access to the original object.
                // If you want to use it just cast it (String) dataObject
                Toast.makeText(MainPage.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainPage.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainPage.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void compareGendersWithAllUsers() {
        if (currentUser != null) {
            // Retrieve the email of the logged-in user
            String userEmail = currentUser.getEmail();

            // Fetch the data of the logged-in user
            db.collection("users").document(userEmail).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot loggedInUserDoc = task.getResult();
                            if (loggedInUserDoc.exists()) {
                                String loggedInUserGender = loggedInUserDoc.getString("gender");
                                String loggedInUserPreference = loggedInUserDoc.getString("preference");
                                //Log.d(TAG, "ALOALO loggedin user name: " + loggedInUserDoc.get("name")+ "gender: " +  loggedInUserGender + " logged in user preference: " + loggedInUserPreference);

                                // Query all users
                                db.collection("users").get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {

                                                for (QueryDocumentSnapshot userDoc : task1.getResult()) {
                                                    String userGender = userDoc.getString("gender");
                                                    String userPreference = userDoc.getString("preference");
                                                    String displayedUserEmail = userDoc.getString("email");
                                                    Log.d(TAG, "ALOALO db user name: " + userDoc.get("name") + " gender: " +  userGender + " db user preference: "+ userPreference);
                                                    if (loggedInUserGender != null && userPreference != null &&
                                                            loggedInUserGender.equals(userPreference) &&
                                                            userGender != null && loggedInUserPreference != null &&
                                                            userGender.equals(loggedInUserPreference)) {

                                                       /*cards item = new cards(userDoc.getString("email"),
                                                                userDoc.getString("name"),
                                                                userDoc.getString("images"));
                                                        rowItems.add(item);
                                                        arrayAdapter.notifyDataSetChanged();*/


                                                        //Log.d(TAG, "Displays" + loggedInUserDoc.get("name") + " matches the logged-in user: " + userDoc.get("name"));

                                                        // Gender and preference match
                                                        Log.d(TAG, "User with email " + userDoc.get("name") + " matches the logged-in user's gender and preference.");
                                                    } else {

                                                        Log.d(TAG, "User with email " + userDoc.get("name") + " does not match the logged-in user's gender and preference.");
                                                    }
                                                }
                                            } else {
                                                // Failed to fetch users
                                                Log.d(TAG, "Error getting users: ", task1.getException());
                                            }
                                        });
                            } else {
                                // Logged-in user document does not exist
                                Log.d(TAG, "Logged-in user document does not exist.");
                            }
                        } else {
                            // Failed to fetch logged-in user data
                            Log.d(TAG, "Error getting logged-in user data: ", task.getException());
                        }
                    });
        } else {
            // No user is logged in
            Log.d(TAG, "No user logged in.");
        }

    }



    private void moreInfo() {

        View popupView = LayoutInflater.from(this).inflate(R.layout.more_info, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(popupView);

        Dialog dialog = dialogBuilder.create();
        dialog.show();


        DocumentReference docRef = db.collection("users").document("b.bilgin@student.tue.nl"); // change

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        TextView nameTextView = popupView.findViewById(R.id.textViewName);
                        nameTextView.setText("Name: " + document.get("name"));
                        TextView ageTextView = popupView.findViewById(R.id.textViewAge);
                        ageTextView.setText("Age: " + document.get("age"));
                        TextView genderTextView = popupView.findViewById(R.id.textViewGender);
                        genderTextView.setText("Name: " + document.get("gender"));
                        TextView heightTextView = popupView.findViewById(R.id.textViewHeight);
                        heightTextView.setText("Name: " + document.get("height"));
                        TextView starSignTextView = popupView.findViewById(R.id.textViewStarSign);
                        starSignTextView.setText("Name: " + document.get("starSign"));
                        TextView smokingTextView = popupView.findViewById(R.id.textViewSmoking);
                        smokingTextView.setText("Name: " + document.get("smoking"));
                        TextView marijuanaTextView = popupView.findViewById(R.id.textViewMarijuana);
                        marijuanaTextView.setText("Name: " + document.get("marijuana"));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


    private void showMatchPopup() {
        // Inflate the match_popup.xml layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.match_popup, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(popupView);

        // Create and show the dialog
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        // Set random values for location and time from arrays (assuming these arrays exist)
        String[] locations = {"Luna Ground Floor",
                "Hubble",
                "Neuron Cafe",
                "Neuron Upstairs Common Area",
                "Traverse Entrance",
                "Atlas Food City",
                "Metaforum Food City",
                "Gemini Food City",
                "Flux Food City",
                "Metaforum Ground Floor",
                "Auditorium In Front of Subway"};
        String[] times = {"Lunch Time Wednesday",
                "Lunch Time Monday",
                "Lunch Time Tuesday",
                "Lunch Time Thursday",
                "Lunch Time Friday",
                "18.00 Wednesday",
                "18.00 Monday",
                "18.00 Tuesday",
                "18.00 Thursday",
                "18.00 Friday" };
        Random random = new Random();
        String randomLocation = locations[random.nextInt(locations.length)];
        String randomTime = times[random.nextInt(times.length)];

        // Find the TextViews and set the location and time

        TextView locationTextView = popupView.findViewById(R.id.textViewMatchLocation);
        locationTextView.setText("Location: " + randomLocation);
        TextView timeTextView = popupView.findViewById(R.id.textViewMatchTime);
        timeTextView.setText("Time: " + randomTime);

    }





}