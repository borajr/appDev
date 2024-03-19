package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        View btnCamera = findViewById(R.id.btn_camera);

        // Set click listener for Camera button
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        CameraActivity.this,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(
                            CameraActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE
                    );
                }
            }
        });

    }
}
