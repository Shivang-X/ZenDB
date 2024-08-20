package src.Authentication;

import src.Interfaces.Authentication;
import src.Main;
import src.Queries.Query;
import src.Utils.Logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

/**
 * This class implements the Authentication interface and provides methods for user registration and login.
 */
public class AuthenticationImpl implements Authentication {
    public static String username = null;
    private String password;
    public static boolean isAuthenticated = false;

    /**
     * Performs user registration by taking username, password, and captcha from the user.
     * Writes the user credentials to a file after successful registration.
     *
     * @throws NoSuchAlgorithmException If MD5 hashing algorithm is not available.
     */
    public void performRegistration() throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username");
        String username = scanner.nextLine();
        setUsername(username);

        System.out.println("Enter password");
        String password = scanner.nextLine();
        setPassword(hashPassword(password));

        String captcha = generateCaptcha();
        System.out.println("Captcha : " + captcha);
        System.out.println("Enter captcha: ");
        String user_captcha = scanner.nextLine();

        if(user_captcha.equals(captcha)) {
            FileWriter fileWriter;

            try {
                fileWriter = new FileWriter("../UserData.txt", true);
                fileWriter.write(String.format("%s;%s;\n", username, hashPassword(password)));
                fileWriter.close();
                System.out.println(Main.ANSI_GREEN + "User registered successfully !!" + Main.ANSI_RESET);
                Logs.writeLog(AuthenticationImpl.username, " Registered ", "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(Main.ANSI_RED + "Incorrect captcha code !!" + Main.ANSI_RESET);
        }
    }

    /**
     * Performs user login by taking username, password, and captcha from the user.
     * Checks user credentials against the stored data and logs the user in if valid.
     */
    public void performLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username");
        String username = scanner.nextLine();
        setUsername(username);

        System.out.println("Enter password");
        String password = scanner.nextLine();
        setPassword(password);

        String captcha = generateCaptcha();
        System.out.println("Captcha : " + captcha);
        System.out.println("Enter generated captcha: ");
        String user_captcha = scanner.nextLine();

        if(captcha.equals(user_captcha)) {
            File userCredentials = new File("../UserData.txt");
            try {
                Scanner fileReader = new Scanner(userCredentials);
                while (fileReader.hasNextLine()) {
                    String data = fileReader.nextLine();
                    String[] values = data.split(";");

                    if (username.equals(values[0]) && hashPassword(password).equals(values[1])) {
                        setAuthenticated(true);
                        Logs.writeLog(AuthenticationImpl.username, " Logged In " , "");
                        System.out.println("Logged In");
                        System.out.println("Hello " + ((!isAuthenticated) ? ("User") : (AuthenticationImpl.username)));
                        while (true) {
                            System.out.println();
                            System.out.println("Execute a query : ");
                            String query = scanner.nextLine();
                            if (query.equals("EXIT") || query.equals("exit")) break;
                            Query queryResolver = new Query();
                            queryResolver.execute(query);
                        }
                    }
                }
                System.out.println(Main.ANSI_RED + "Incorrect Credentials" + Main.ANSI_RESET);
            } catch (FileNotFoundException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(Main.ANSI_RED + "Incorrect captcha code !!" + Main.ANSI_RESET);
        }
    }

    /**
     * Hashes the provided password using the MD5 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password.
     * @throws NoSuchAlgorithmException If MD5 hashing algorithm is not available.
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        BigInteger bigInt = new BigInteger(1, bytes);
        String hex = bigInt.toString(16);
        // Pad with leading zeros to ensure length of 32 characters
        return String.format("%32s", hex).replace(' ', '0');
    }

    /**
     * Generates a random captcha with a length of 6 characters.
     *
     * @return The generated captcha.
     */
    private static String generateCaptcha() {
        // Define the characters allowed in the captcha
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Set the length of the captcha
        int captchaLength = 6;

        StringBuilder captcha = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < captchaLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            captcha.append(allowedChars.charAt(randomIndex));
        }

        return captcha.toString();
    }

    /**
     * Sets the username for the current instance.
     *
     * @param username The username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password for the current instance.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the current instance.
     *
     * @param password The password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Checks if the user is authenticated.
     *
     * @return True if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Sets the authentication status for the current instance.
     *
     * @param authenticated The authentication status to be set.
     */
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
