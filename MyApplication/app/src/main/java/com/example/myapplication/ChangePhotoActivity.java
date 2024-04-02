package com.example.myapplication;

// Existing imports...
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Add these new imports
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ChangePhotoActivity extends AppCompatActivity {

    // Existing constants...
    private static final int PICK_IMAGE = 3; // Choose an unused request code
    private static final int CAMERA_REQUEST = 2;

    private static final int CAMERA_PERMISSION_CODE = 100;

    // New instance variables for the image view placeholders
    private ImageView[] imageViews = new ImageView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);

        // Initialize image view placeholders with actual ImageView references from your layout
        imageViews[0] = findViewById(R.id.imageView);
        imageViews[1] = findViewById(R.id.imageView1);
        imageViews[2] = findViewById(R.id.imageView2);
        imageViews[3] = findViewById(R.id.imageView3);
        imageViews[4] = findViewById(R.id.imageView4);
        imageViews[5] = findViewById(R.id.imageView5);


        Button btnChoosePhoto = findViewById(R.id.btn_choose_photo);
        Button btnTakePhoto = findViewById(R.id.btn_take_photo);
        Button btnContinue = findViewById(R.id.btn_continue);

        final ImageView imageView = findViewById(R.id.imageView);
        Button imageViewCloseBtn = findViewById(R.id.imageViewCloseBtn);

        imageViewCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        final ImageView imageView1 = findViewById(R.id.imageView1);
        Button imageViewCloseBtn1 = findViewById(R.id.imageViewCloseBtn1);

        imageViewCloseBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        final ImageView imageView2 = findViewById(R.id.imageView2);
        Button imageViewCloseBtn2 = findViewById(R.id.imageViewCloseBtn2);

        imageViewCloseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        final ImageView imageView3 = findViewById(R.id.imageView3);
        Button imageViewCloseBtn3 = findViewById(R.id.imageViewCloseBtn3);

        imageViewCloseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        final ImageView imageView4 = findViewById(R.id.imageView4);
        Button imageViewCloseBtn4 = findViewById(R.id.imageViewCloseBtn4);

        imageViewCloseBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView4.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        final ImageView imageView5 = findViewById(R.id.imageView5);
        Button imageViewCloseBtn5 = findViewById(R.id.imageViewCloseBtn5);

        imageViewCloseBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView5.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });
        // Set click listener for Choose Photo button
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for camera permission before opening the camera
                if (ContextCompat.checkSelfPermission(ChangePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(ChangePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    // If permission is granted, open the camera
                    openCamera();
                }
            }
        });


        // Set click listener for Continue button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here you would handle what happens when the Continue button is clicked
                // This might involve validating that at least one photo has been selected,
                // and then proceeding to the next step of your application.
                // For example:
                boolean atLeastOneImageSelected = false;
                for (ImageView imageView : imageViews) {
                    if (imageView.getDrawable() != null) {
                        atLeastOneImageSelected = true;
                        break;
                    }
                }
                if (atLeastOneImageSelected) {
                    Intent intent = new Intent(ChangePhotoActivity.this, ProfileSetup.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePhotoActivity.this, "Please upload at least 1 photo.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    // Overriding onActivityResult to handle image selection/taking photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Handle the camera photo
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setNextAvailableImageView(imageBitmap);
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            // Handle the chosen image
            Uri selectedImage = data.getData();
            setNextAvailableImageView(selectedImage);
        }
    }

    // Handling runtime permissions results for the camera
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is needed to take photos", Toast.LENGTH_LONG).show();
            }
        }
        // Handle other permissions that your app might request here
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (intent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(intent, CAMERA_REQUEST);
        //} else {
        Toast.makeText(this, "No camera found on this device", Toast.LENGTH_LONG).show();
        //}
    }


    private void setNextAvailableImageView(Bitmap imageBitmap) {
        for (ImageView imageView : imageViews) {
            if (imageView.getDrawable() == null) { // Check if ImageView is empty
                imageView.setImageBitmap(imageBitmap);
                break;
            }
        }
    }

    // Same method for Uri images from gallery
    private void setNextAvailableImageView(Uri imageUri) {
        for (ImageView imageView : imageViews) {
            if (imageView.getDrawable() == null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    inputStream.close();
                    break;
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(this, "Error getting selected file.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    private void downloadImagesFromFirebaseStorage() {
        ImageView image1 = findViewById(R.id.imageView);
        ImageView image2 = findViewById(R.id.imageView1);
        ImageView image3 = findViewById(R.id.imageView2);
        ImageView image4 = findViewById(R.id.imageView3);
        ImageView image5 = findViewById(R.id.imageView4);
        ImageView image6 = findViewById(R.id.imageView5);
        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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

