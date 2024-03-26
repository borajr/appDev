package com.example.myapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class CameraActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 3;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private FirebaseStorage storage;
    private ImageView[] imageViews = new ImageView[6];
    private Bitmap[] images = new Bitmap[6];
    private int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        storage = FirebaseStorage.getInstance();

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
                    Intent intent = new Intent(CameraActivity.this, PreferenceOne.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CameraActivity.this, "Please upload at least 1 photo.", Toast.LENGTH_LONG).show();
            }}
        });
    }

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

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void setImage(Bitmap imageBitmap) {
        if (imageIndex < 6) {
            imageViews[imageIndex].setImageBitmap(imageBitmap);
            images[imageIndex] = imageBitmap;
            imageIndex++;
        }
    }

    private void uploadImagesToFirebaseStorage() {
        for (int i = 0; i < imageIndex; i++) {
            if (images[i] != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                images[i].compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                String imageFileName = UUID.randomUUID().toString(); // Generate a random file name
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
}
