/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp1;
import java.util.Scanner;

/**
 *
 * @author Student
 */
public class MainApp {
       public static void main(String[] args) {

        // input
        Scanner input = new Scanner(System.in);

        // login object
        Login login = new Login();

        System.out.println("\n=== USER REGISTRATION ===");

        // registration loop
        while (true) {

            System.out.print("Enter a username: ");
            String username = input.nextLine();

            System.out.print("Enter a password: ");
            String password = input.nextLine();

            System.out.print("Enter phone (+27...): ");
            String phone = input.nextLine();

            // register user
            String response = login.registerUser(username, password, phone);
            System.out.println(response);

            // stop if success
            if (response.equals("User registered successfully.")) {
                break;
            }

            System.out.println("Please try again");
        }

        System.out.println("\n=== USER LOGIN ===");

        // login input
        System.out.print("Username: ");
        String loginUsername = input.nextLine();

        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        // check login
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);

        // show result
        System.out.println(login.returnLoginStatus(loggedIn));

        input.close();
    }
} 

