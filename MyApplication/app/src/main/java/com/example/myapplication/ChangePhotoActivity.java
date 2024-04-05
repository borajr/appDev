package com.example.myapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Activity to change or add new photos by the user. It allows capturing new photos with the device's camera,
 * selecting photos from the device's gallery, and uploading these to Firebase Storage. The activity also
 * downloads and displays existing photos from Firebase Storage.
 */
public class ChangePhotoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 3;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private FirebaseStorage storage;
    private ImageView[] imageViews = new ImageView[6];
    private Bitmap[] images = new Bitmap[6];
    private int imageIndex = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    /**
     * Initializes the activity, sets up UI components, and downloads existing images from Firebase Storage.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);

        storage = FirebaseStorage.getInstance();

        imageViews[0] = findViewById(R.id.imageView);
        imageViews[1] = findViewById(R.id.imageView1);
        imageViews[2] = findViewById(R.id.imageView2);
        imageViews[3] = findViewById(R.id.imageView3);
        imageViews[4] = findViewById(R.id.imageView4);
        imageViews[5] = findViewById(R.id.imageView5);

        downloadImagesFromFirebaseStorage();

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChangePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChangePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    openCamera();
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImagesToFirebaseStorage();
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
                }}
        });
    }

    /**
     * Handles result from photo selection or capture activities.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing to identify who this result came from.
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
     * Processes the result of the camera permission request.
     *
     * @param requestCode  The request code passed in requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
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
     * Initiates capturing a photo with the device's camera.
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * Sets the selected or captured image to an ImageView and stores it for uploading.
     *
     * @param imageBitmap The bitmap of the image to be displayed and uploaded.
     */
    private void setImage(Bitmap imageBitmap) {
        if (imageIndex < 6) {
            imageViews[imageIndex].setImageBitmap(imageBitmap);
            images[imageIndex] = imageBitmap;
            imageIndex++;
        }
    }

    /**
     * Uploads the selected or captured images to Firebase Storage under the current user's email directory.
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
                                Toast.makeText(ChangePhotoActivity.this, "Images uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangePhotoActivity.this, "Failed to upload images", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    /**
     * Downloads and displays images from Firebase Storage.
     */
    private void downloadImagesFromFirebaseStorage() {
        //initilazing the image array
        ImageView image1 = findViewById(R.id.imageView);
        ImageView image2 = findViewById(R.id.imageView1);
        ImageView image3 = findViewById(R.id.imageView2);
        ImageView image4 = findViewById(R.id.imageView3);
        ImageView image5 = findViewById(R.id.imageView4);
        ImageView image6 = findViewById(R.id.imageView5);
        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        //ui image adding
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference okref = storage.getReference().child(currentUser.getEmail()+"/");

        //initiliaing the photo array
        final int[] countofimages = {0};
        okref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    countofimages[0] =  listResult.getItems().size();//will give you number of files present in your firebase storage folder
                }
            }
        });
        //for loop for all images
        int x = 0;
        for (int i = 0; i < countofimages[0]; i++) { // Assuming you have 6 images to download

            // Create a reference to the image in Firebase Storage
            Log.d("ChangePhoto", currentUser.getEmail() + "/image" + x);
            StorageReference imageRef = storage.getReference().child(currentUser.getEmail() + "/image" + x );
            x++;
            // Create a temporary file to store the downloaded image
            int finalX = x;
            imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    //bitmap to store the image
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViews.get(finalX).setImageBitmap(bitmap);
                }
            });
        }
    }
}