package src;

import src.Authentication.AuthenticationImpl;
import src.Queries.Query;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * The Main class contains the main method to run the Distributed Database System.
 */
public class Main {

    // ANSI color codes for console output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * The main method to start the application.
     *
     * @param args Command-line arguments.
     * @throws NoSuchAlgorithmException If a requested cryptographic algorithm is not available.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        AuthenticationImpl auth = new AuthenticationImpl();
        Query queryResolver = new Query();

        System.out.println("Welcome to the Distributed Database System");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Hello " + ((!AuthenticationImpl.isAuthenticated) ? "User" : AuthenticationImpl.username));
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");
            System.out.println();
            System.out.print("Enter the number of operation you want to perform: ");

            int operation = scanner.nextInt();

            switch (operation) {
                case 1:
                    auth.performRegistration();
                    break;

                case 2:
                    auth.performLogin();
                    break;

                case 3:
                    System.out.println("Exiting the Distributed Database System. Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println(ANSI_RED + "Invalid operation. Please enter a valid option." + ANSI_RESET);
            }
        }
    }
}
