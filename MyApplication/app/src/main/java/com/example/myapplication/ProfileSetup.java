package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ProfileSetup extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseUser currentUser;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        setContentView(R.layout.activity_change_page);

        // Find the button by its ID
        View btnChangePass = findViewById(R.id.changepass);
        View btnYouBasics = findViewById(R.id.basics);
        View btnYouInfo = findViewById(R.id.info);
        View btnYouLifestyle = findViewById(R.id.lifestyle);
        View btnThemInfo = findViewById(R.id.info2);
        View btnThemLifestyle2 = findViewById(R.id.lifestyle2);
        View btnChangePhotos = findViewById(R.id.changephoto);
        View btnAccountInfo = findViewById(R.id.goodbye);
        downloadImagesFromFirebaseStorage();

        // Set OnClickListener to the button
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ChangePassword.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouBasics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneOne.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnYouLifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ProfileCloneThree.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnThemLifestyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, PreferenceCloneOne.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnThemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, PreferenceCloneTwo.class);
                startActivity(intent); // Start the new activity
            }
        });

        btnChangePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, ChangePhotoActivity.class);
                startActivity(intent); // Start the new activity
            }
        });

        // Set OnClickListener to the button
        btnAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileSetup.this, AccountInfo.class);
                startActivity(intent); // Start the new activity
            }
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);

        // Set OnClickListener to the button
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(ProfileSetup.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    startActivity(new Intent(ProfileSetup.this, MainPage.class));
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(ProfileSetup.this, AllChatsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
    private void downloadImagesFromFirebaseStorage() {
        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);
        ImageView image3 = findViewById(R.id.image3);
        ImageView image4 = findViewById(R.id.image4);
        ImageView image5 = findViewById(R.id.image5);
        ImageView image6 = findViewById(R.id.image6);
        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        int x = 1;
        for (ImageView i : imageViews) { // Assuming you have 6 images to download

            // Create a reference to the image in Firebase Storage
            Log.d("ProfileSetup", currentUser.getEmail() + "/image" + x);
            StorageReference imageRef = storage.getReference().child(currentUser.getEmail() + "/image" + x + ".jpeg");
            x++;
            // Create a temporary file to store the downloaded image
            imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    i.setImageBitmap(bitmap);
                }
            });
        }
    }

}


