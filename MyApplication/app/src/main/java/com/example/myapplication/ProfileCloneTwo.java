package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProfileCloneTwo extends AppCompatActivity {

    private Spinner genderSpinner, minHeightSpinner, maxHeightSpinner, minAgeSpinner, maxAgeSpinner;
    private Button confirmButton;

    private OrientationEventListener orientationEventListener;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiletwoclone);

        // Find the button by its ID
        View btnContinue = findViewById(R.id.confirm_button);

        genderSpinner = findViewById(R.id.editText1);
        minHeightSpinner = findViewById(R.id.minHeightSpinner);
        maxHeightSpinner = findViewById(R.id.maxHeightSpinner);
        minAgeSpinner = findViewById(R.id.minAgeSpinner);
        maxAgeSpinner = findViewById(R.id.maxAgeSpinner);
        confirmButton = findViewById(R.id.confirm_button);
        textView = findViewById(R.id.textview);
        selectedLanguage = new boolean[langArray.length];

        String gender = genderSpinner.getSelectedItem().toString();
        String height = minHeightSpinner.getSelectedItem().toString();
        Integer age = Integer.parseInt(minAgeSpinner.getSelectedItem().toString());


        // Set OnClickListener to the button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePreferences()) {
                    // Create an Intent to navigate to the activity_change_password.xml
                    DatabaseHandler db = new DatabaseHandler();
                    Map<String, Object> data = createMap(gender, height, age); //TODO: ADD LANGLIST
                    db.updateUser(data);
                    Intent nextIntent = new Intent(ProfileCloneTwo.this, ProfileSetup.class); // Adjust as needed
                    startActivity(nextIntent);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCloneTwo.this);

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

    private boolean validatePreferences() {
        int minHeightIndex = minHeightSpinner.getSelectedItemPosition();
        int maxHeightIndex = maxHeightSpinner.getSelectedItemPosition();
        if (minHeightIndex > maxHeightIndex) {
            Toast.makeText(this, "Minimum height cannot be greater than maximum height", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate ages
        int minAgeIndex = minAgeSpinner.getSelectedItemPosition();
        int maxAgeIndex = maxAgeSpinner.getSelectedItemPosition();
        if (minAgeIndex > maxAgeIndex) {
            Toast.makeText(this, "Minimum age cannot be greater than maximum age", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }
    private Map<String, Object> createMap(String gender, String height, Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("gender", gender);
        map.put("height", height);
        map.put("age", age);
        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}
