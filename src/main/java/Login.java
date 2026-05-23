/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class Login {
  
    String username;
    String password;
    String phoneNumber;
// CHECK USERNAME RULES
    public boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }
// CHECK PASSWORD RULES 
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
//CHECK PHONE NUMBER
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() <= 12;
    }
// REGISTER USER
    public String registerUser(String username, String password, String phoneNumber) {

        if (!checkUsername(username)) {
            return "Username is not correctly formatted.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted.";
        }

        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        return "User registered successfully.";
    }
// LOGIN CHECK
    public boolean loginUser(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
// LOGIN MESSAGE
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + this.username + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
