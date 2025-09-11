package org.example;

import org.example.auth.repository.FileUserRepository;
import org.example.auth.repository.UserRepository;
import org.example.auth.service.UserService;
import org.example.auth.service.UserServiceImpl;
import org.example.auth.ui.Menu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path desktop = Paths.get(System.getProperty("user.home"), "Desktop");

        Path dataFile = desktop.resolve("users.json");

        try {
            Files.createDirectories(dataFile.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserRepository userRepository = new FileUserRepository(dataFile.toString());
        UserService userService = new UserServiceImpl(userRepository);
        Menu menu = new Menu(userService);

        System.out.println("Data file path: " + dataFile);
        menu.start();
    }
}
