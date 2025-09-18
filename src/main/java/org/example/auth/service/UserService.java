package org.example.auth.service;

import org.example.auth.constant.UserState;

public interface UserService {

    UserState.RegistrationResult register(String username, String password);
    UserState.LoginResult login(String username, String password);
}