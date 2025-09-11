package org.example.auth.service;

public interface UserService {
    enum RegistrationResult {
        SUCCESS,
        USERNAME_ALREADY_EXISTS
    }

    enum LoginResult {
        SUCCESS,
        INVALID_CREDENTIALS,
        USER_LOCKED
    }

    RegistrationResult register(String username, String password);
    LoginResult login(String username, String password);
}