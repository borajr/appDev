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

        name.setText(card_item.getName());
        switch(card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                //Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

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