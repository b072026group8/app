package com.cscb07.taamapp.auth;

interface Presenter {
    void login(String email, String password);
    void signup(String name, String email, String password);
}
