package src.Interfaces;

import java.security.NoSuchAlgorithmException;

/**
 * This interface defines the methods related to user authentication.
 */
public interface Authentication {

    /**
     * Performs user registration by taking input for username and password,
     * hashing the password, and storing the user credentials.
     *
     * @throws NoSuchAlgorithmException If the specified cryptographic algorithm is not available.
     */
    public void performRegistration() throws NoSuchAlgorithmException;

    /**
     * Performs user login by taking input for username and password,
     * validating the credentials, and allowing access if authentication is successful.
     */
    public void performLogin();
}
