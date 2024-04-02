package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreferenceOne extends AppCompatActivity {

    private Spinner genderSpinner, minHeightSpinner, maxHeightSpinner, minAgeSpinner, maxAgeSpinner, departmentSpinner;
    private Button confirmButton;

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

    String[] starSignArray = {
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Virgo",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces"};

    String[] genderArray = {
            "Male",
            "Female",
            "Non-binary",
            "Other" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_detail);

        minHeightSpinner = findViewById(R.id.minHeightSpinner);
        maxHeightSpinner = findViewById(R.id.maxHeightSpinner);
        minAgeSpinner = findViewById(R.id.minAgeSpinner);
        maxAgeSpinner = findViewById(R.id.maxAgeSpinner);
        confirmButton = findViewById(R.id.confirm_button);
        departmentSpinner = findViewById(R.id.editText5);
        textView = findViewById(R.id.textview);
        selectedLanguage = new boolean[langArray.length];
        TextView textstar = findViewById(R.id.textstar);
        TextView textgender = findViewById(R.id.textgender);
        

        String gender = textgender.getText().toString();
        String starSign = textstar.getText().toString();
        String department = departmentSpinner.getSelectedItem().toString();
        String minHeight = minHeightSpinner.getSelectedItem().toString();
        String maxHeight = maxHeightSpinner.getSelectedItem().toString();
        Integer minAge =  Integer.parseInt(minAgeSpinner.getSelectedItem().toString());
        Integer maxAge = Integer.parseInt(maxAgeSpinner.getSelectedItem().toString());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePreferences()) { //createUserPref only used here, after this use Update
                    createUserPref(getData(gender, minHeight, maxHeight,starSign, department, minAge, maxAge));
                    Intent nextIntent = new Intent(PreferenceOne.this, PreferenceTwo.class);
                    startActivity(nextIntent);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceOne.this);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceOne.this);

                // set title
                builder.setTitle("Select Star-Sign");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(starSignArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(starSignArray[langList.get(j)]);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceOne.this);

                // set title
                builder.setTitle("Select Gender");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(genderArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(genderArray[langList.get(j)]);
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
        // Ensure a gender is selected

        // Validate heights
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


    private Map<String, Object> getData(String gender, String minHeight, String maxHeight, String starSign,
                                          String department, Integer minAge, Integer maxAge) {
        Map<String, Object> map = new HashMap<>();
        int minHeightInt = 0;
        int maxHeightInt = 0;
        if (minHeight.equals("<150")) {
            minHeightInt = 149;
        } else if (minHeight.equals(">210")) {
            minHeightInt = 211;
        } else {
            minHeightInt = Integer.parseInt(minHeight);
        }
        if (maxHeight.equals("<150")) {
            maxHeightInt = 149;
        } else if (minHeight.equals(">210")) {
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
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void createUserPref(Map<String, Object> data) {
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("preferences")
                .document(email)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("prefoOne", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("PrefOne", "Error writing document", e);
                    }
                });
    }
}