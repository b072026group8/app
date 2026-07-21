package com.cscb07.taamapp.auth;

public class AuthPresenter implements Presenter {
    private final AuthModel model;

    public AuthPresenter() {
        model = new AuthModel();
    }

    @Override
    public void signup(String name, String email, String password) {
        // User signup
        model.registerUser(name, email, password);
    }

    @Override
    public void login(String email, String password) {
        // User login
        model.loginUser(email, password);
    }
}
