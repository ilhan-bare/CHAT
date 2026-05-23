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
        String username;
    String password;
    String phoneNumber;

    // check username
    public boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // check password
    public boolean checkPasswordComplexity(String password) {

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSpecial = true;
            }
        }

        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    // check phone
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() <= 12;
    }

    // register
    public String registerUser(String username, String password, String phoneNumber) {

        if (!checkUsername(username)) {
            return "Username not correct.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password not correct.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Phone not correct.";
        }

        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        return "User registered successfully.";
    }

    // login
    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // message
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + username + "!";
        } else {
            return "Login failed.";
        }
    }
}

