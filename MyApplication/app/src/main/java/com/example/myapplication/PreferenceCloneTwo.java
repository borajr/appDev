package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class PreferenceCloneTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_clonetwo);

        TextView textView;
        boolean[] selectedLanguage;
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = {
                "Amharic",
                "Arabic",
                "Armenian",
                "Azerbaijani",
                "Bengali",
                "Bhojpuri",
                "Bulgarian",
                "Burmese",
                "Chinese (Mandarin)",
                "Dutch",
                "English",
                "Farsi (Persian)",
                "Finnish",
                "French",
                "German",
                "Gujarati",
                "Hausa",
                "Hebrew",
                "Hindi",
                "Indonesian",
                "Italian",
                "Japanese",
                "Javanese",
                "Kannada",
                "Kazakh",
                "Khmer",
                "Korean",
                "Lao",
                "Malagasy",
                "Malay",
                "Marathi",
                "Maithili",
                "Nepali",
                "Norwegian",
                "Odia (Oriya)",
                "Pashto",
                "Polish",
                "Portuguese",
                "Punjabi",
                "Russian",
                "Serbian",
                "Sindhi",
                "Sinhalese",
                "Somali",
                "Spanish",
                "Sudanese",
                "Swahili",
                "Swedish",
                "Tamil",
                "Telugu",
                "Thai",
                "Tigrinya",
                "Turkish",
                "Ukrainian",
                "Urdu",
                "Uzbek",
                "Vietnamese"};

        // Find the button by its ID
        View btnConfirm = findViewById(R.id.confirm_button);
        textView = findViewById(R.id.textview);
        selectedLanguage = new boolean[langArray.length];


        // Set OnClickListener to the button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(PreferenceCloneTwo.this, ProfileSetup.class);
                startActivity(intent); // Start the new activity
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceCloneTwo.this);

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textView.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }
}
