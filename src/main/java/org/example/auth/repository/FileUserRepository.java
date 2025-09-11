package org.example.auth.repository;


import org.example.auth.model.User;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FileUserRepository implements UserRepository {

    private final String filePath;
    private final Map<String, User> userCache;
    private static final String SEPARATOR = ",";

    public FileUserRepository(String filePath) {
        this.filePath = filePath;
        this.userCache = loadUsersFromFile();
        log.info("UserRepository initialized. Loaded {} users from file.", userCache.size());
    }

    private Map<String, User> loadUsersFromFile() {
        Map<String, User> users = new HashMap<>();
        File file = new File(filePath);

        if (!file.exists()) {
            log.warn("User data file not found at {}. Starting with an empty user map.", filePath);
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(SEPARATOR, 2);
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(user.getUsername().toLowerCase(), user);
                }
            }
        } catch (IOException e) {
            log.error("Failed to read user data from file: {}", filePath, e);
        }
        return users;
    }


    private void writeUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : userCache.values()) {
                writer.write(user.getUsername() + SEPARATOR + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Failed to write user data to file: {}", filePath, e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userCache.get(username.toLowerCase()));
    }

    @Override
    public void save(User user) {
        userCache.put(user.getUsername().toLowerCase(), user);
        log.info("User '{}' saved in cache. Persisting to file.", user.getUsername());
        writeUsersToFile();
    }
}