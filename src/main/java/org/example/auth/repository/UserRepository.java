package org.example.auth.repository;


import org.example.auth.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}