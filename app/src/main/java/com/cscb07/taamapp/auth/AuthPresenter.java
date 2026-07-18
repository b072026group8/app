package com.cscb07.taamapp.auth;

public class AuthPresenter implements Auth.Presenter {
    private Auth.View view;
    private AuthModel model;

    public AuthPresenter(Auth.View view) {
        this.view = view;
        model = new AuthModel();
    }

    @Override
    public void signup(String name, String email, String password) {
        model.registerUser(name, email, password);
    }

    @Override
    public void login(String email, String password) {
        model.loginUser(email, password);
    }
}
