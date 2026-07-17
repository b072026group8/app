package com.cscb07.taamapp.auth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cscb07.taamapp.R;

public class AuthPresenter implements Auth.Presenter {
    private Auth.View view;
    private AuthModel model;

    public AuthPresenter(Auth.View view) {
        this.view = view;
        model = new AuthModel();
    }

    @Override
    public void signup(String name, String email, String password) {

    }

    @Override
    public void login(String email, String password) {

    }
}
