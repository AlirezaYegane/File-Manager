package org.example;
import org.example.auth.repository.FileUserRepository;
import org.example.auth.repository.UserRepository;
import org.example.auth.service.UserService;
import org.example.auth.service.UserServiceImpl;
import org.example.auth.ui.Menu;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository("users.txt");
        UserService userService = new UserServiceImpl(userRepository);
        Menu menu = new Menu(userService);
        menu.start();
    }
}