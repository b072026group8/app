package com.cscb07.taamapp.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cscb07.taamapp.R;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment extends Fragment {
    private EditText editTextName, editTextEmail, editTextPassword;
    private Button signupButton;
    private FirebaseDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        editTextName = view.findViewById(R.id.SignupName);
        editTextEmail = view.findViewById(R.id.SignupEmailAddress);
        editTextPassword = view.findViewById(R.id.SignupPassword);
        signupButton = view.findViewById(R.id.SignupButton);

        db = FirebaseDatabase.getInstance("https://b07-g8-project-default-rtdb.firebaseio.com/");

        return view;
    }
}
