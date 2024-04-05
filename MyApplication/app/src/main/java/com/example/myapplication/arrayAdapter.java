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


/**
 * An ArrayAdapter for displaying a list of User objects, each with a name and profile image.
 */
public class arrayAdapter extends ArrayAdapter<User> {
    Context context;
    private MainPage mainPage;

    // Firebase Storage initialization
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    /**
     * Constructs an ArrayAdapter for User objects.
     *
     * @param mainPage The MainPage context this adapter is used in.
     * @param users    The list of User objects to display.
     */
    public arrayAdapter(MainPage mainPage, List<User> users) {
        super(mainPage, R.layout.item, users);
        this.mainPage = mainPage;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.).
     *
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView imageView = convertView.findViewById(R.id.image);
        name.setText(card_item.getName() != null ? card_item.getName() : "");

        // Assuming getImageStoragePath() method returns a valid path for Firebase Storage
        String imagePath = card_item.getProfileImageUrl();
        StorageReference imageRef = storage.getReferenceFromUrl(imagePath);

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
                Glide.with(getContext()).load(R.drawable.logo).into(imageView); // Adjust this to your default placeholder image resource
            }
        });

        Button moreInfoButton = convertView.findViewById(R.id.infoButton);
        moreInfoButton.setOnClickListener(v -> {
            mainPage.moreInfo(card_item);
        });

        return convertView;
    }
}