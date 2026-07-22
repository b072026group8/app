package com.cscb07.taamapp.auth;

import android.util.Patterns;

public class AuthPresenter implements Presenter, AuthStatus {
    private final AuthModel model;
    private final UserAuthentication view;

    public AuthPresenter(UserAuthentication view, AuthModel model) {
        this.model = model;
        this.view = view;
        model.initStatus(this);
    }

    @Override
    public void signup(String name, String email, String password) {
        // User signup
        if (name.isEmpty()) {
            view.showError("Name can't be empty");
        } else if (email.isEmpty()) {
            view.showError("Email can't be empty");
        } else if (password.isEmpty()) {
            view.showError("Password can't be empty");
        } else if (password.length() < 6) {  // Prevent bad passwords
            view.showError("Password must be at least 6 characters");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {  // Email must follow this format something@something.something
            view.showError("Please enter a valid email address");
        } else {
            model.registerUser(name, email, password);
        }
    }

    @Override
    public void login(String email, String password) {
        // User login
        if (email.isEmpty()) {
            view.showError("Email can't be empty");
        } else if (password.isEmpty()) {
            view.showError("Password can't be empty");
        } else {
            model.loginUser(email, password);
        }
    }

    // Error Toast for invalid user credentials in signup and login
    public void failedAuth(String m) {
        view.showError(m);
    }
}
