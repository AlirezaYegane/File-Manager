package org.example.auth.ui;


import org.example.auth.service.UserService;

import java.util.Scanner;

public class Menu {

    private final UserService userService;
    private final Scanner scanner;

    public Menu(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    System.out.println("bye!");
                    return;
                default:
                    System.out.println("invalid input");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- main menu ---");
        System.out.println("1. Login");
        System.out.println("2.Register");
        System.out.println("3. exit");
        System.out.print("Your choice: ");
    }

    private void handleRegister() {
        System.out.println("\n--- register new user ---");
        System.out.print("enter user name: ");
        String username = scanner.nextLine();
        System.out.print("enter password: ");
        String password = scanner.nextLine();

        UserService.RegistrationResult result = userService.register(username, password);

        switch (result) {
            case SUCCESS:
                System.out.println("registeration is successfull.");
                break;
            case USERNAME_ALREADY_EXISTS:
                System.out.println("user is already exist");
                break;
        }
    }

    private void handleLogin() {
        System.out.println("\n--- login ---");
        System.out.print("user name: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();

        UserService.LoginResult result = userService.login(username, password);

        switch (result) {
            case SUCCESS:
                System.out.println("welcome.");
                break;
            case INVALID_CREDENTIALS:
                System.out.println("username or password is false. ");
                break;
            case USER_LOCKED:
                System.out.println("Your account reach the limitation for 5 min.");
                break;
        }
    }
}