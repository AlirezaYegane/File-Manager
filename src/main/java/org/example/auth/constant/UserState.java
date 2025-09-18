package org.example.auth.constant;

public interface UserState {

    enum RegistrationResult {
        SUCCESS,
        USERNAME_ALREADY_EXISTS
    }

    enum LoginResult {
        SUCCESS,
        INVALID_CREDENTIALS,
        USER_LOCKED
    }
}
