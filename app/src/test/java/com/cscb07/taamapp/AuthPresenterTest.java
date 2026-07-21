package com.cscb07.taamapp;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.cscb07.taamapp.auth.AuthModel;
import com.cscb07.taamapp.auth.AuthPresenter;
import com.cscb07.taamapp.auth.UserAuthentication;

public class AuthPresenterTest {
    private AuthModel mockModel;
    private UserAuthentication mockView;
    private AuthPresenter presenter;

    @Before
    public void beforeTest() {
        mockView = mock(UserAuthentication.class);
        mockModel = mock(AuthModel.class);

        presenter = new AuthPresenter(mockView, mockModel);
    }
    @Test
    public void Signup1() {
        presenter.signup("", "", "");
        verify(mockView).showError("Name can't be empty");
        verify(mockModel, never()).registerUser(anyString(), anyString(), anyString());  // New user not created
    }
    @Test
    public void Signup2() {
        presenter.signup("Daniel", "", "");
        verify(mockView).showError("Email can't be empty");
        verify(mockModel, never()).registerUser(anyString(), anyString(), anyString());  // New user not created
    }
    @Test
    public void Signup3() {
        presenter.signup("Daniel", "daniel@daniel.com", "");
        verify(mockView).showError("Password can't be empty");
        verify(mockModel, never()).registerUser(anyString(), anyString(), anyString());  // New user not created
    }
    @Test
    public void Signup4() {
        presenter.signup("Daniel", "daniel@daniel.com", "abc");
        verify(mockView, never()).showError(anyString());  // Error message not displayed
        verify(mockModel).registerUser("Daniel", "daniel@daniel.com", "abc");  // New user not created
    }
    @Test
    public void Login1() {
        presenter.login("", "");
        verify(mockView).showError("Email can't be empty");
        verify(mockModel, never()).loginUser(anyString(), anyString());  // New user not logged in
    }

    @Test
    public void Login2() {
        presenter.login("daniel@daniel.com", "");
        verify(mockView).showError("Password can't be empty");
        verify(mockModel, never()).loginUser(anyString(), anyString());  // New user not logged in
    }

    @Test
    public void Login3() {
        presenter.login("daniel@daniel.com", "abc");
        verify(mockView, never()).showError(anyString());
        verify(mockModel).loginUser("daniel@daniel.com", "abc");  // New user not logged in
    }
}
