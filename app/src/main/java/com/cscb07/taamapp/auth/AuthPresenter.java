package com.cscb07.taamapp.auth;

public class AuthPresenter implements Presenter {
    private final AuthModel model;
    private final UserAuthentication view;

    public AuthPresenter(UserAuthentication view, AuthModel model) {
        this.model = model;
        this.view = view;
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
}
