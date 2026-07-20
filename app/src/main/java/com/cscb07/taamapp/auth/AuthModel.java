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
        // Firebase authentication documentation
        mAuth.createUserWithEmailAndPassword(email, password)  // Create user
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {  // Null check
                                saveUserRTDB(user.getUid(), name, email);
                                System.out.println("Signup success");
                                // TODO Update UI after signing up (Home page)
                                System.out.println(user.getUid());
                            }
                        } else {
                            System.out.println("Signup failed");
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

    public void loginUser(String email, String password) {
        // Firebase authentication documentation
        mAuth.signInWithEmailAndPassword(email, password)  // Sign in
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {  // Null check
                                System.out.println("Login success");
                                System.out.println(user.getUid());
                                // TODO Update UI after logging in (Home page)
                            } else {
                                System.out.println("Login success, user is null");
                            }
                        } else {
                            System.out.println("Login failed");
                        }
                    }
                });
    }

}
