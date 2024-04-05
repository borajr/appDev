package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private OrientationEventListener orientationEventListener;


    private SwipeFlingAdapterView flingContainer;
    private ArrayList<User> users; // Assuming you have a User class with info to display
    private ArrayAdapter<User> arrayAdapter;
    private boolean temp = false; // This boolean is set based on swipe
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private MainMatch mainMatch = new MainMatch(this);

    CollectionReference usersReference = db.collection("users");
    ListView listView;
    List<User> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                User swipedUser = (User) dataObject;
                String swipedUserEmail = swipedUser.getEmail();
                // Do something with the dataObject if needed
                mainMatch.recordSwipe(currentUser.getEmail(), swipedUser.getUserEmail(), "left");

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                // Set temp to true when swiped right
                temp = true;
                User swipedUser = (User) dataObject;
                String swipedUserEmail = swipedUser.getEmail();
                // Do something with the dataObject if needed
                mainMatch.recordSwipe(currentUser.getEmail(), swipedUser.getUserEmail(), "right");

                mainMatch.checkForMatch(currentUser.getEmail(), swipedUser.getUserEmail());

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
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 45 && orientation < 135) {
                    // Landscape mode, set screen orientation to reverse portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation >= 135 && orientation < 225) {
                    // Upside down mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else if (orientation >= 225 && orientation < 315) {
                    // Reverse landscape mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else {
                    // Portrait mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        };
        // Start the OrientationEventListener
        orientationEventListener.enable();
        compareGendersWithAllUsers();
    }
    public void compareGendersWithAllUsers() {
        if (currentUser != null) {
            // Retrieve the email of the logged-in user
            String userEmail = currentUser.getEmail();

            // Fetch the data of the logged-in user
            db.collection("preferences").document(userEmail).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot loggedInUserPrefDoc = task.getResult();
                            if (loggedInUserPrefDoc.exists()) {
                                List<String> loggedInUserGenderPrefs = (List<String>) loggedInUserPrefDoc.get("gender");

                                // Query all users
                                db.collection("users").get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                users.clear(); // Clear the list before adding matched users

                                                for (QueryDocumentSnapshot userDoc : task1.getResult()) {
                                                    String userGender = userDoc.getString("gender");
                                                    String displayedUserEmail = userDoc.getString("email");

                                                    // Make sure we do not add the current user
                                                    if (!displayedUserEmail.equals(userEmail) &&
                                                            loggedInUserGenderPrefs.contains(userGender)) {

                                                        // Gender matches the preference
                                                        // Now create a User object and add it to the users list
                                                        User matchedUser = createUserFromDocument(userDoc);
                                                        users.add(matchedUser);
                                                    }
                                                }
                                                arrayAdapter.notifyDataSetChanged(); // Notify the adapter
                                            } else {
                                                // Failed to fetch users
                                                Log.d(TAG, "Error getting users: ", task1.getException());
                                            }
                                        });
                            } else {
                                // Logged-in user's preferences do not exist
                                Log.d(TAG, "Logged-in user's preferences do not exist.");
                            }
                        } else {
                            // Failed to fetch logged-in user's preferences
                            Log.d(TAG, "Error getting logged-in user's preferences: ", task.getException());
                        }
                    });
        } else {
            // No user is logged in
            Log.d(TAG, "No user logged in.");
        }
    }

    private User createUserFromDocument(DocumentSnapshot document) {
        // Here you need to extract all the user information from the document and create a User object.
        // For example:
        return new User(
                document.getString("email"),
                document.getString("name"),
                document.getString("profileImageUrl"), // Update this to fetch from Firebase Storage if needed
                document.getString("department"),
                document.getString("food"),
                document.getBoolean("alcohol") != null ? document.getBoolean("alcohol") : false,
                document.getBoolean("smoking") != null ? document.getBoolean("smoking") : false,
                document.getBoolean("marijuana") != null ? document.getBoolean("marijuana") : false,
                document.getLong("age") != null ? document.getLong("age").intValue() : 20, // Add a default value for age
                document.getLong("height") != null ? document.getLong("height").intValue() : 160, // Add a default value for height
                document.getString("gender")
        );
    }

    private void fetchUsersFromBackend() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // First, get the current user's gender preferences
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference preferencesRef = db.collection("preferences").document(currentUserEmail);

        preferencesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> genderPreferences = (List<String>) documentSnapshot.get("genderPreferences");
                    if (genderPreferences != null && !genderPreferences.isEmpty()) {
                        // Now use these preferences to filter the users
                        fetchFilteredUsers(genderPreferences);
                    } else {
                        // No gender preferences set, handle accordingly
                    }
                } else {
                    // Preferences not found, handle accordingly
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle error
            }
        });
    }

    private void fetchFilteredUsers(List<String> genderPreferences) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch users with the specified genders
        db.collection("users").whereIn("gender", genderPreferences).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users.clear(); // Clear the existing list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Create a new User object from the document data
                                String userEmail = document.getString("email");
                                // ... (the rest of your code to create a User instance)
                                String name = document.getString("name");
                                String gender = document.getString("gender");
                                String profileImageUrl = document.getString("image"); // Update this to fetch from Firebase Storage if needed
                                String department = document.getString("department");
                                String food = document.getString("food");

                                boolean alcohol = document.getBoolean("alcohol") != null ? document.getBoolean("alcohol") : false;
                                boolean smoking = document.getBoolean("smoking") != null ? document.getBoolean("smoking") : false;
                                boolean weed = document.getBoolean("marijuana") != null ? document.getBoolean("marijuana") : false;

                                // Assuming age and height are stored as numbers (long in Firestore)
                                Long ageLong = document.getLong("age"); // This could be null if "age" field is missing
                                int DEFAULT_AGE = 20;
                                int age = (ageLong != null) ? ageLong.intValue() : DEFAULT_AGE;
                                Long heightLong = document.getLong("height");
                                int DEFAULT_HEIGHT = 160;
                                int height = (heightLong != null) ? heightLong.intValue() :DEFAULT_HEIGHT;// Add null check if necessary

                                User user = new User(userEmail, name, profileImageUrl, department, food,
                                        alcohol, smoking, weed, age, height, gender);

                                users.add(user);
                            }
                            arrayAdapter.notifyDataSetChanged(); // Notify the adapter to update the UI
                        } else {
                            Log.w(TAG, "Error getting filtered documents.", task.getException());
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
        genderTextView.setText("Gender: " + user.getGender());
        TextView heightTextView = popupView.findViewById(R.id.textViewHeight);
        heightTextView.setText("Height: " + user.getHeight());
        TextView starSignTextView = popupView.findViewById(R.id.textViewStarSign);
        starSignTextView.setText("StarSign: Aries");
        TextView smokingTextView = popupView.findViewById(R.id.textViewSmoking);
        smokingTextView.setText("Smoknig: " + user.getSmoking());
        TextView marijuanaTextView = popupView.findViewById(R.id.textViewMarijuana);
        marijuanaTextView.setText("Marijuana: " + user.getWeed());
        TextView alcoholTextView = popupView.findViewById(R.id.textViewAlcohol);
        alcoholTextView.setText("Alcohol: " + user.getAlcohol());
        TextView foodTextView = popupView.findViewById(R.id.textViewFood);
        foodTextView.setText("Diet: " + user.getPreferredDiet());


    }

    public void showMatchPopup() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}