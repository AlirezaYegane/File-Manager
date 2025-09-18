package org.example.auth.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.exception.DataReadException;
import org.example.auth.exception.NoDataFoundException;
import org.example.auth.exception.WriteException;
import org.example.auth.model.User;
import org.example.auth.serializer.MySerializer;

import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FileUserRepository implements UserRepository {

    private final String filePath;
    private final Map<String, User> userCache;
    private final MySerializer serializer = new MySerializer();

    public FileUserRepository(String filePath) {
        this.filePath = filePath;
        this.userCache = loadUsersFromFile();
        log.info("UserRepository initialized. Loaded {} users from file.", userCache.size());
    }

    private Map<String, User> loadUsersFromFile() {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                log.warn("User data file not found at {}. Starting with an empty user map.", filePath);
                throw new FileNotFoundException("User data file not found at " + filePath);
            }
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            Map<String, User> data = serializer.loadFromJson(filePath, type);
            if(data == null) {
                throw new NoDataFoundException("User data file not found at " + filePath);
            }
            return data;
        } catch (IOException e) {
            log.error("Failed to read user data from file: {}", filePath, e);
            throw new DataReadException("Failed to read user data from file: " + filePath, e);
        }
    }

    private void writeUsersToFile() {
        try {
            serializer.saveAsJson(userCache, filePath);
        } catch (IOException e) {
            log.error("Failed to write user data to file: {}", filePath, e);
            throw  new WriteException("Failed to write user data to file: " + filePath);
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
