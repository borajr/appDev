package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<User> {
    Context context;
    private MainPage mainPage;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public arrayAdapter(MainPage mainPage, List<User> users) {
        super(mainPage, R.layout.item, users);
        this.mainPage = mainPage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView imageView = convertView.findViewById(R.id.image);

        name.setText(card_item.getName() != null ? card_item.getName() : "");

        // Construct the image path using the user's email
        // Note: Assuming the getProfileImageUrl() returns the email address of the user
        String userEmail = card_item.getProfileImageUrl(); // You might need to adjust this if getProfileImageUrl() does not return the email
        String imagePath = userEmail.replace("@", "%40") + "/image0"; // URL encode the "@" symbol
        StorageReference imageRef = storage.getReference().child(imagePath);

        // Use Glide to load the image from the storage reference
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri.toString()).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle errors, e.g., by loading a default image.
                Glide.with(getContext()).load(R.drawable.logo).into(imageView); // Use your default placeholder image
            }
        });

        Button moreInfoButton = convertView.findViewById(R.id.infoButton);
        moreInfoButton.setOnClickListener(v -> {
            mainPage.moreInfo(card_item);
        });

        return convertView;
    }
}