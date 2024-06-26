package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreferenceCloneOne extends AppCompatActivity {

    private Spinner genderS;
    private Spinner minHeightS;
    private Spinner maxHeightS;
    private Spinner minAgeS;
    private Spinner maxAgeS;
    private Spinner departmentS;
    private Spinner starSignS;
    private Button cnfrm;


    TextView textView;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {
            "Amharic", "Arabic", "Armenian", "Azerbaijani", "Bengali", "Bhojpuri",
            "Bulgarian", "Burmese", "Chinese (Mandarin)", "Dutch", "English", "Farsi (Persian)", "Finnish", "French", "German", "Gujarati",
            "Hausa", "Hebrew", "Hindi", "Indonesian", "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Khmer", "Korean", "Lao", "Malagasy", "Malay",
            "Marathi", "Maithili", "Nepali", "Norwegian", "Odia (Oriya)", "Pashto", "Polish", "Portuguese", "Punjabi", "Russian",
            "Serbian", "Sindhi", "Sinhalese", "Somali", "Spanish", "Sudanese", "Swahili", "Swedish", "Tamil", "Telugu", "Thai", "Tigrinya", "Turkish",
            "Ukrainian", "Urdu", "Uzbek", "Vietnamese"};

    String[] langArray1 = {
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};

    String[] langArray2 = {
            "Male",
            "Female",
            "Non-binary",
            "Other" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_cloneone);

        minHeightS = findViewById(R.id.minHeightSpinner);
        maxHeightS = findViewById(R.id.maxHeightSpinner);
        minAgeS = findViewById(R.id.minAgeSpinner);
        maxAgeS = findViewById(R.id.maxAgeSpinner);
        cnfrm = findViewById(R.id.confirm_button);
        departmentS = findViewById(R.id.editText5);
        textView = findViewById(R.id.textview);
        selectedLanguage = new boolean[langArray.length];
        TextView textstar = findViewById(R.id.textstar);
        TextView textgender = findViewById(R.id.textgender);

        DatabaseHandler db = new DatabaseHandler();

        Integer Minage =  Integer.parseInt(minAgeS.getSelectedItem().toString());
        Integer Maxage = Integer.parseInt(maxAgeS.getSelectedItem().toString());
        String Gender = textgender.toString();
        String Starsign = textstar.toString();
        String Maxheight = maxHeightS.getSelectedItem().toString();
        String Department = departmentS.getSelectedItem().toString();
        String Minheight = minHeightS.getSelectedItem().toString();


        cnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePreferences()) { //createUserPref only used here, after this use Update
                    db.updateUserPref(getData(Gender, Minheight, Maxheight,Starsign, Department, Minage, Maxage));
                    Intent nextIntent = new Intent(PreferenceCloneOne.this, ProfileSetup.class);
                    startActivity(nextIntent);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceCloneOne.this);

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

        textstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceCloneOne.this);

                // set title
                builder.setTitle("Select Star-Sign");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray1, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(langArray1[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textstar.setText(stringBuilder.toString());
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
                            textstar.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });

        textgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceCloneOne.this);

                // set title
                builder.setTitle("Select Gender");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray2, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(langArray2[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textgender.setText(stringBuilder.toString());
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
                            textgender.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });

    }

    private boolean validatePreferences() {

        // Validate ages
        int minAgeIndex = minAgeS.getSelectedItemPosition();
        int maxAgeIndex = maxAgeS.getSelectedItemPosition();
        if (minAgeIndex > maxAgeIndex) {
            Toast.makeText(this, "Minimum age cannot be greater than maximum age", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Ensure a gender is selected
        // Validate heights
        int minHeightIndex = minHeightS.getSelectedItemPosition();
        int maxHeightIndex = maxHeightS.getSelectedItemPosition();
        if (minHeightIndex > maxHeightIndex) {
            Toast.makeText(this, "Minimum height cannot be greater than maximum height", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }

    private Map<String, Object> getData(String gender, String minHeight, String maxHeight, String starSign,
                                        String department, Integer minAge, Integer maxAge) {
        Map<String, Object> map = new HashMap<>();
        int minHeightInt = 0;
        int maxHeightInt = 0;
        if (minHeight.equals("<150")) {
            minHeightInt = 149;
        } else if (minHeight.equals("<210")) {
            minHeightInt = 211;
        } else {
            minHeightInt = Integer.parseInt(minHeight);
        }
        if (maxHeight.equals("<150")) {
            maxHeightInt = 149;
        } else if (maxHeight.equals("<210")) {
            maxHeightInt = 211;
        } else {
            maxHeightInt = Integer.parseInt(minHeight);
        }
        map.put("gender", gender);
        map.put("minHeight", minHeightInt);
        map.put("maxHeight", maxHeightInt);
        map.put("starSign", starSign);
        map.put("department", department);
        map.put("minAge", minAge);
        map.put("maxAge", maxAge);
        return map;
    }
}