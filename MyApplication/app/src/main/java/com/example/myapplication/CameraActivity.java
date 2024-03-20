package com.example.myapplication;

// Existing imports...
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CameraActivity extends AppCompatActivity {

    // Existing constants...
    private static final int PICK_IMAGE = 3; // Choose an unused request code
    private static final int CAMERA_REQUEST = 2;

    private static final int CAMERA_PERMISSION_CODE = 100;

    // New instance variables for the image view placeholders
    private ImageView[] imageViews = new ImageView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

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
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
                    Intent intent = new Intent(CameraActivity.this, PreferenceOne.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CameraActivity.this, "Please upload at least 1 photo.", Toast.LENGTH_LONG).show();
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
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "No camera found on this device", Toast.LENGTH_LONG).show();
        }
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

}

