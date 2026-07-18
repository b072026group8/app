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

public class LoginFragment extends Fragment implements Auth.View {

    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private AuthPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Login  input fields
        editTextEmail = view.findViewById(R.id.LoginEmailAddress);
        editTextPassword = view.findViewById(R.id.LoginPassword);
        loginButton = view.findViewById(R.id.newAccountButton);

        presenter = new AuthPresenter(this);

        // Login button input
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                presenter.login(email, password);
            }
        });

        return view;
    }
}
