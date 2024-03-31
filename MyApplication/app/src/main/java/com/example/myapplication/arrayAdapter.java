package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;
public class arrayAdapter extends ArrayAdapter<User>{
    Context context;
    private MainPage mainPage;

    public arrayAdapter(MainPage mainPage, List<User> users) {
        super(mainPage, R.layout.item, users);
        this.mainPage = mainPage;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        User card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        if (card_item.getName() != null) {
            name.setText(card_item.getName());
        } else {
            name.setText(""); // Set default text or handle accordingly
        }


        if (card_item.getProfileImageUrl() != null && !card_item.getProfileImageUrl().equals("default")) {
            // Load image with Glide
            Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
        } else {
            // Load default image if profileImageUrl is "default" or null
            Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
        }

        //More info button
        Button moreInfoButton = convertView.findViewById(R.id.infoButton);
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getItem(position);
                mainPage.moreInfo(user);
            }
        });


        return convertView;

    }

    // Interface to communicate info button click to the activity
    public interface OnInfoButtonClickListener {
        void onInfoButtonClick(int position);
    }

    private OnInfoButtonClickListener mCallback;

    public void setOnInfoButtonClickListener(OnInfoButtonClickListener listener) {
        mCallback = listener;
    }
}