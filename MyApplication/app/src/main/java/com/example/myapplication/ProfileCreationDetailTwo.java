package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProfileCreationDetailTwo extends AppCompatActivity {

    //spinner for gender
    private Spinner spinnerGender;
    //spinner for minHegiht
    private Spinner spinnerMinHeight;
    //spinner for department
    private Spinner spinnerDepartment;
    private Spinner spinnerStarSign;
    private Button buttonConfirm;

    private OrientationEventListener orientationEventListener;


    // initialize variables
    TextView textView;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"Amharic", "Arabic",
            "Armenian",
            "Azerbaijani", "Bengali",
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
        setContentView(R.layout.activity_profiledetailtwo); // Ensure this matches your layout file

        selectedLanguage = new boolean[langArray.length];
        spinnerDepartment = findViewById(R.id.editText5);
        spinnerStarSign = findViewById(R.id.starSignSpinner);
        // Initialize your views
        spinnerGender = findViewById(R.id.editText1); // TODO: this is called edittext1 but is a spinner?
        spinnerMinHeight = findViewById(R.id.minHeightSpinner);
        buttonConfirm = findViewById(R.id.confirm_button);
        textView = findViewById(R.id.textview);



        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Move to the next appropriate page
                    String gender = spinnerGender.getSelectedItem().toString();
                    String height = spinnerMinHeight.getSelectedItem().toString();
                    String department = spinnerDepartment.getSelectedItem().toString();
                    String starSign = spinnerStarSign.getSelectedItem().toString();
                    DatabaseHandler db = new DatabaseHandler();
                    Map<String, Object> data = createMap(gender, height, department, starSign); //TODO: ADD LANGLIST
                    db.updateUser(data);
                    Intent nextIntent = new Intent(ProfileCreationDetailTwo.this, ProfileCreationDetailThree.class); // Adjust as needed
                    startActivity(nextIntent);

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCreationDetailTwo.this);

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
    private Map<String, Object> createMap(String Gender, String Height, String Department, String starSign) {
        Map<String, Object> map = new HashMap<>();
        map.put("department", Department);
        int heightInt = 0;

        if (Height.equals("<150")) {
            heightInt = 149;
        } else if (Height.equals(">210")) {
            heightInt = 211;
        } else {
            heightInt = Integer.parseInt(Height);
        }
        map.put("height", heightInt);


        map.put("gender", Gender);
        return map;
    }

}
