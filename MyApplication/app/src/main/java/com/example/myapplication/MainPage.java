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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int i = 0;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private SwipeFlingAdapterView flingContainer;
    private ArrayList<User> users; // Assuming you have a User class with info to display
    private ArrayAdapter<User> arrayAdapter;
    private boolean temp = false; // This boolean is set based on swipe
    FirebaseStorage storage = FirebaseStorage.getInstance();



    CollectionReference usersReference = db.collection("users");
    ListView listView;
    List<User> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean matched = true;


        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        // Initialize your users array here
        users = new ArrayList<>();

        arrayAdapter = new arrayAdapter(this, users);

        fetchUsersFromBackend();

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                users.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                // Set temp to false when swiped left
                temp = false;
                // Do something with the dataObject if needed
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                // Set temp to true when swiped right
                temp = true;
                // Do something with the dataObject if needed
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // You can implement some sort of feedback or animation while swiping if desired
            }
        });

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_main);

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

        compareGendersWithAllUsers();

        if (matched) {
            //moreInfo();
            showMatchPopup();
        }


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



    private void fetchUsersFromBackend() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userEmail = document.getString("email");
                        String name = document.getString("name");
                        String profileImageUrl = document.getString("image"); // Update this to fetch from Firebase Storage if needed
                        String department = document.getString("department");
                        String food = document.getString("food");

                        // Use a default value if the field is null
                        boolean alcohol = document.getBoolean("alcohol") != null ? document.getBoolean("alcohol") : false;
                        boolean smoking = document.getBoolean("smoking") != null ? document.getBoolean("smoking") : false;
                        boolean weed = document.getBoolean("weed") != null ? document.getBoolean("weed") : false;

                        // Assuming age and height are stored as numbers (long in Firestore)
                        Long ageLong = document.getLong("age"); // This could be null if "age" field is missing
                        int DEFAULT_AGE = 20;
                        int age = (ageLong != null) ? ageLong.intValue() : DEFAULT_AGE;
                        int DEFAULT_HEIGHT = 160;
                        int height = (ageLong != null) ? ageLong.intValue() : DEFAULT_HEIGHT; // Add null check if necessary

                        User user = new User(userEmail, name, profileImageUrl, department, food,
                                alcohol, smoking, weed, age, height);

                        users.add(user);
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }



    public void moreInfo(User user) {

        View popupView = LayoutInflater.from(this).inflate(R.layout.more_info, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(popupView);

        Dialog dialog = dialogBuilder.create();
        dialog.show();


        TextView nameTextView = popupView.findViewById(R.id.textViewName);
        nameTextView.setText("Name: " + user.getName());
        TextView ageTextView = popupView.findViewById(R.id.textViewAge);
        ageTextView.setText("Age: " + user.getAge());
        TextView genderTextView = popupView.findViewById(R.id.textViewGender);
        genderTextView.setText("Gender: female");
        TextView heightTextView = popupView.findViewById(R.id.textViewHeight);
        heightTextView.setText("Height: " + user.getHeight());
        TextView starSignTextView = popupView.findViewById(R.id.textViewStarSign);
        starSignTextView.setText("StarSign: Aries");
        TextView smokingTextView = popupView.findViewById(R.id.textViewSmoking);
        smokingTextView.setText("Smoknig: " + user.getSmoking());
        TextView marijuanaTextView = popupView.findViewById(R.id.textViewMarijuana);
        marijuanaTextView.setText("Marijuana: " + user.getWeed());
        TextView alcoholTextView = popupView.findViewById(R.id.textViewAlcohol);
        marijuanaTextView.setText("Marijuana: " + user.getAlcohol());
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

