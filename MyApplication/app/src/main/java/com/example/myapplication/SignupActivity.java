package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText editTextEmail = findViewById(R.id.editText1);
        EditText passwordEditText = findViewById(R.id.editText2); // Assuming this is the ID for the password field
        EditText repeatPasswordEditText = findViewById(R.id.editText3); // Assuming this is the ID for the repeat password field
        Button signUpButton = findViewById(R.id.signupButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correctInput = true;
                String emailInput = editTextEmail.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();
                mAuth = FirebaseAuth.getInstance();

                // Check if the email contains "tue.nl"
                if (!emailInput.contains("tue.nl")) {
                    // Intent to navigate to the next Activity (replace NextActivity.class with ConfirmationActivity.class)
                    // Corrected to ConfirmationActivity
                    correctInput = false;
                    Toast.makeText(SignupActivity.this, "Please enter a TU/e email address.", Toast.LENGTH_LONG).show();
                }

                if(emailInput.isEmpty()) { // Extend this condition for all fields
                    correctInput = false;
                    Toast.makeText(SignupActivity.this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
                }

                // Then, check if passwords match and are strong enough
                if (!password.equals(repeatPassword)) {
                    correctInput = false;
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                }

                if (!isValidPassword(password)) {
                    correctInput = false;
                    Toast.makeText(SignupActivity.this, "Password must contain 8+ characters, a letter, a number, and a capital letter.", Toast.LENGTH_LONG).show();
                }
                if (correctInput && !isStaff(emailInput)) {
                    createUser(emailInput, password);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String,Object> user = new HashMap<>();
                    user.put("banned", false);
                    user.put("email", emailInput);
                    db.collection("users")
                            .document(emailInput)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Intent intent = new Intent(SignupActivity.this, ConfirmationActivity.class);
                                    finish();
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });


                    // If all checks pass, proceed to the ConfirmationActivity

                } else if (correctInput && isStaff(emailInput)) {
                    createUser(emailInput, password);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String,Object> user = new HashMap<>();
                    user.put("banned", false);
                    user.put("email", emailInput);
                    //user.put("isStaff", true);
                    db.collection("users_staff")
                            .document(emailInput)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Intent intent = new Intent(SignupActivity.this, ConfirmationActivity.class);
                                    finish();
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);

                                }
                            });
                }
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false; // Password too short
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasCapitalLetter = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasCapitalLetter = true;
            }
            if (hasLetter && hasDigit && hasCapitalLetter) {
                return true; // Password meets all criteria
            }
        }
        return false; // Password missing required character types
    }
    boolean flag1 = false;
    boolean flag2 = false;
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            flag1 = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mAuth.signInWithEmailAndPassword(email, password);
    }

    private boolean isStaff(String email){
        return !email.endsWith("@student.tue.nl");
    }
}
