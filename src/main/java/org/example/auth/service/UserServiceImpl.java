package org.example.auth.service;


import org.example.auth.model.User;
import org.example.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Map<String, Integer> failedLoginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lockedUsers = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_DURATION_MINUTES = 5;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegistrationResult register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("Registration failed: Username '{}' already exists.", username);
            return RegistrationResult.USERNAME_ALREADY_EXISTS;
        }

        User newUser = new User(username, password);
        userRepository.save(newUser);
        log.info("User '{}' registered successfully.", username);
        return RegistrationResult.SUCCESS;
    }

    @Override
    public LoginResult login(String username, String password) {
        String lowerCaseUsername = username.toLowerCase();

        if (isUserLocked(lowerCaseUsername)) {
            log.warn("Login failed for user '{}': Account is locked.", username);
            return LoginResult.USER_LOCKED;
        }

        Optional<User> userOptional = userRepository.findByUsername(lowerCaseUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(password)) {
                log.info("User '{}' logged in successfully.", username);
                resetLoginAttempts(lowerCaseUsername);
                return LoginResult.SUCCESS;
            } else {
                log.warn("Invalid password attempt for user '{}'.", username);
                handleFailedLogin(lowerCaseUsername);
                return LoginResult.INVALID_CREDENTIALS;
            }

        } else {
            log.warn("Login failed: User '{}' not found.", username);
            return LoginResult.INVALID_CREDENTIALS;
        }
    }

    private void handleFailedLogin(String username) {
        int attempts = failedLoginAttempts.getOrDefault(username, 0) + 1;
        if (attempts >= MAX_ATTEMPTS) {
            long lockTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(LOCK_DURATION_MINUTES);
            lockedUsers.put(username, lockTime);
            failedLoginAttempts.remove(username);
            log.error("User '{}' locked for {} minutes due to {} failed login attempts.", username, LOCK_DURATION_MINUTES, MAX_ATTEMPTS);
        } else {
            failedLoginAttempts.put(username, attempts);
            log.warn("Failed login attempt {}/{} for user '{}'.", attempts, MAX_ATTEMPTS, username);
        }
    }

    private boolean isUserLocked(String username) {
        if (lockedUsers.containsKey(username)) {
            long lockTime = lockedUsers.get(username);
            if (System.currentTimeMillis() < lockTime) {
                return true;
            } else {
                lockedUsers.remove(username);
                log.info("Lock expired for user '{}'. Account is now unlocked.", username);
                return false;
            }
        }
        return false;
    }

    private void resetLoginAttempts(String username) {
        failedLoginAttempts.remove(username);
        lockedUsers.remove(username);
    }
}