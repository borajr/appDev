package com.example.myapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * CameraActivity allows users to take photos using the device's camera or choose photos from the gallery.
 * Users can upload these photos to Firebase Storage. The activity handles permission requests for the camera
 * and adjusts the orientation of the activity based on the device's orientation.
 */
public class CameraActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 3;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private FirebaseStorage storage;
    private ImageView[] viewImages = new ImageView[6];
    private Bitmap[] images = new Bitmap[6];
    private int imageIndex = 0;
    private OrientationEventListener orientationEventListener;

    /**
     * Sets up the activity layout, initializes Firebase storage, image view placeholders, and button click
     * listeners. It also initializes an orientation event listener to handle the orientation changes.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        storage = FirebaseStorage.getInstance();

        viewImages[0] = findViewById(R.id.imageView);
        viewImages[1] = findViewById(R.id.imageView1);
        viewImages[2] = findViewById(R.id.imageView2);
        viewImages[3] = findViewById(R.id.imageView3);
        viewImages[4] = findViewById(R.id.imageView4);
        viewImages[5] = findViewById(R.id.imageView5);

        Button taake = findViewById(R.id.btn_take_photo);
        Button chuuz = findViewById(R.id.btn_choose_photo);
        Button cont = findViewById(R.id.btn_continue);

        final ImageView imageView = findViewById(R.id.imageView);
        Button imageViewCloseBtn = findViewById(R.id.imageViewCloseBtn);

        imageViewCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        Button imageViewCloseBtn1 = findViewById(R.id.imageViewCloseBtn1);

        imageViewCloseBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImages[0].setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        Button imageViewCloseBtn2 = findViewById(R.id.imageViewCloseBtn2);

        imageViewCloseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImages[1].setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        Button imageViewCloseBtn3 = findViewById(R.id.imageViewCloseBtn3);

        imageViewCloseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImages[2].setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        Button imageViewCloseBtn4 = findViewById(R.id.imageViewCloseBtn4);

        imageViewCloseBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImages[3].setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        Button imageViewCloseBtn5 = findViewById(R.id.imageViewCloseBtn5);

        imageViewCloseBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImages[4].setImageDrawable(null); // Clears the ImageView
                // Optionally, hide the button itself if desired
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImagesToFirebaseStorage();
                boolean atLeastOneImageSelected = false;
                for (ImageView imageView : viewImages) {
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
                }}
        });

        // Set click listener for Choose Photo button
        //Yapacağımız işe sıçayım
        chuuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        taake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    openCamera();
                }
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
    }

    /**
     * Handles the result from either taking a photo with the camera or picking an image from the gallery.
     * Sets the chosen or captured image in the next available image view.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setImage(imageBitmap);
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                setImage(bitmap);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the result of the permission request for using the camera.
     *
     * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int).
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
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
    }

    /**
     * Opens the device camera to take a photo.
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * Sets the given Bitmap image to the next available ImageView and stores the Bitmap in an array for future use.
     *
     * @param imageBitmap The Bitmap image to set in an ImageView and store in the array.
     */
    private void setImage(Bitmap imageBitmap) {
        if (imageIndex < 6) {
            viewImages[imageIndex].setImageBitmap(imageBitmap);
            images[imageIndex] = imageBitmap;
            imageIndex++;
        }
    }

    /**
     * Uploads the images stored in the Bitmap array to Firebase Storage under the current user's email directory.
     */
    private void uploadImagesToFirebaseStorage() {
        for (int i = 0; i < imageIndex; i++) {
            if (images[i] != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                images[i].compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                String imageFileName = "image"+i; // Generate a random file name
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                // Upload byte array to Firebase Storage
                StorageReference storageRef = storage.getReference().child(currentUser.getEmail() + "/" + imageFileName);
                storageRef.putBytes(imageData)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(CameraActivity.this, "Images uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CameraActivity.this, "Failed to upload images", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    /**
     * Disables the OrientationEventListener when the activity is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}