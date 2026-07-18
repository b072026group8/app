package com.cscb07.taamapp.auth;

public interface Auth {
    interface View {
        //public void loadHome();
    }
    interface Presenter {
        public void login(String email, String password);
        public void signup(String name, String email, String password);
    }
}
