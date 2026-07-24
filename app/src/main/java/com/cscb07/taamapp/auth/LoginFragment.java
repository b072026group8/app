package com.cscb07.taamapp.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cscb07.taamapp.HomeFragment;
import com.cscb07.taamapp.R;

public class LoginFragment extends Fragment implements UserAuthentication {

    private EditText editTextEmail, editTextPassword;
    private Button loginButton, signupButton;
    private AuthPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Login  input fields
        editTextEmail = view.findViewById(R.id.LoginEmailAddress);
        editTextPassword = view.findViewById(R.id.LoginPassword);
        loginButton = view.findViewById(R.id.LoginButton);
        signupButton = view.findViewById(R.id.NewAccountButton);

        presenter = new AuthPresenter(this, new AuthModel());

        // Login button input
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                presenter.login(email, password);
            }
        });

        // New Account button input
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change to signup fragment
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignupFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void showError(String m) {
        if (getContext() != null) {
            Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadHome() {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
    }
}
