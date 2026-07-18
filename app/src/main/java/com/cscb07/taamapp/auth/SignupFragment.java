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

public class SignupFragment extends Fragment implements Auth.View {
    private EditText editTextName, editTextEmail, editTextPassword;
    private Button signupButton;
    private AuthPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Sign up input fields
        editTextName = view.findViewById(R.id.SignupName);
        editTextEmail = view.findViewById(R.id.SignupEmailAddress);
        editTextPassword = view.findViewById(R.id.SignupPassword);
        signupButton = view.findViewById(R.id.signupButton);

        presenter = new AuthPresenter(this);

        // Signup button input
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                presenter.signup(name, email, password);
            }
        });

        return view;
    }
}
