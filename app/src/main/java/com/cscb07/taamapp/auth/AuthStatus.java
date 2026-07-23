package com.cscb07.taamapp.auth;

public interface AuthStatus {
    // Login and signup status (e.g did the user login successfully or not)
    void successAuth();
    void failedAuth(String m);
}
