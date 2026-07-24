package com.cscb07.taamapp.auth;

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
        } else if (!isValidEmail(email)) {  // Email must follow this format something@something.something
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

    public void successAuth() {
        view.loadHome();
    }

    // Checks if email input is valid. Must follow something@something.something
    private boolean isValidEmail(String e) {
        String regex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";
        return e.matches(regex);  // Literally copied from Patterns.EMAIL_ADDRESS. Having issues with testing
    }
}
