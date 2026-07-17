package com.cscb07.taamapp.auth;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AuthModel {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    public AuthModel() {
        // Initialize firebase instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)  // Create user
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                pres
                            }
                        } else {

                        }
                    }
                });
    }

    public void saveUserRTDB(String userID, String name, String email) {
        Map<String, String> user = new HashMap<>();
        user.put("userid", userID);
        user.put("name", name);
        user.put("email", email);

        // Put in RTDB. Firebase RTDB documentation
        DatabaseReference mDatabase = db.getReference();
        mDatabase.child("users").child(userID).setValue(user);
    }

}
