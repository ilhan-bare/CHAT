/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp1;

/**
 *
 * @author Student
 */
public class Login {

    // Stores the registered user's username, password, and phone number
    String username;
    String password;
    String phoneNumber;

    /**
     * Checks if the username meets the required format:
     * - Must contain an underscore (_)
     * - Must be 5 characters or fewer
     */
    public boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks if the password meets complexity requirements:
     * - At least 8 characters long
     * - Contains at least one uppercase letter
     * - Contains at least one digit
     * - Contains at least one special character
     */
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through each character to check for required character types
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;        // Found an uppercase letter
            } else if (Character.isDigit(c)) {
                hasNumber = true;         // Found a digit
            } else {
                hasSpecial = true;        // Found a special character
            }
        }

        // All conditions must be met for the password to be valid
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks if the phone number meets the required format:
     * - Must start with +27 (South African format)
     * - Must be 12 characters or fewer in total
     */
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() <= 12;
    }

    /**
     * Attempts to register a user by validating all three fields.
     * If any validation fails, an appropriate error message is returned.
     * On success, the credentials are stored and a success message is returned.
     */
    public String registerUser(String username, String password, String phoneNumber) {

        // Validate username format before proceeding
        if (!checkUsername(username)) {
            return "Username not correct.";
        }

        // Validate password complexity before proceeding
        if (!checkPasswordComplexity(password)) {
            return "Password not correct.";
        }

        // Validate phone number format before proceeding
        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Phone not correct.";
        }

        // All validations passed — store the user's credentials
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        return "User registered successfully.";
    }

    /**
     * looks out if the provided username and password match the registered credentials.
     * Returns true if both match, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Returns a login status message based on whether the login was successful.
     * - Success: greets the user by username
     * - Failure: returns a generic login failed message
     */
    public String returnLoginStatus(boolean success) {
        if (success) {
            // Personalized welcome message for successful login
            return "Welcome " + username + "!";
        } else {
            // Generic failure message — does not reveal which field was wrong
            return "Login failed.";
        }
    }
}