package com.mycompany.chatapp1;

import java.util.Scanner;
import java.util.ArrayList;

public class MainApp {

    public static void main(String[] args) {
        // Initialize scanner for user input and Login object for auth operations
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // =========================
        // USER REGISTRATION
        // =========================
        System.out.println("\n=== USER REGISTRATION ===");

        // Collect the user's first and last name (no validation required)
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        // Keep looping until a valid username, password, and phone number are provided
        while (true) {
            // Username must contain an underscore and be 5 characters or fewer
            System.out.print("Enter a username (must contain _ and be 5 chars or less): ");
            String username = input.nextLine();

            // Validate username format before proceeding to password
            if (login.checkUsername(username)) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println("Username not correct. Please try again.");
                continue; // Restart loop if username is invalid
            }

            // Password must be at least 8 chars, with 1 capital, 1 number, and 1 special character
            System.out.print("Enter a password (min 8 chars, 1 capital, 1 number, 1 special): ");
            String password = input.nextLine();

            // Phone number must follow the +27 South African format
            System.out.print("Enter phone number (+27...): ");
            String phone = input.nextLine();

            // Attempt to register the user with the provided credentials
            String response = login.registerUser(username, password, phone);
            System.out.println(response);

            // If registration is successful, greet the user and exit the loop
            if (response.equals("User registered successfully.")) {
                System.out.println("Welcome " + firstName + " " + lastName + ", your account has been created.");
                break;
            }
            // If registration failed, prompt the user to try again
            System.out.println("Please try again.\n");
        }

        // =========================
        // USER LOGIN
        // =========================
        System.out.println("\n=== USER LOGIN ===");

        // Prompt user to log in with their registered credentials
        System.out.print("Username: ");
        String loginUsername = input.nextLine();
        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        // Validate login credentials and display the result
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loggedIn));

        // If login fails, deny access and terminate the application
        if (!loggedIn) {
            System.out.println("Access denied. Exiting application.");
            input.close();
            return;
        }

        // =========================
        // WELCOME MESSAGE
        // =========================
        System.out.println("\nWelcome to  QuickChat.");

        // =========================
        // HOW MANY MESSAGES
        // =========================
        int maxMessages = 0;

        // Keep prompting until the user enters a valid positive integer
        while (true) {
            System.out.print("How many messages would you like to send? ");
            String countInput = input.nextLine().trim();
            try {
                maxMessages = Integer.parseInt(countInput);
                if (maxMessages > 0) break; // Valid count entered, exit loop
                System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                // Handle non-numeric input gracefully
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        // List to store all successfully sent Message objects
        ArrayList<Message> sentMessages = new ArrayList<>();

        // Tracks the current message number (used for Message ID generation)
        int messageCounter = 1;

        // =========================
        // MAIN MENU LOOP
        // =========================
        while (true) {
            // Display the main menu options to the user
            System.out.println("\n--- MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    // Prevent sending more messages than the user's set limit
                    if (messageCounter > maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + ".");
                        break;
                    }

                    // Create a new Message object with the current message number
                    Message msg = new Message(messageCounter);

                    // Validate the recipient's cell number before accepting it
                    String recipient;
                    while (true) {
                        System.out.print("Enter recipient cell number (+27...): ");
                        recipient = input.nextLine().trim();
                        String cellCheck = msg.checkRecipientCell(recipient);
                        System.out.println(cellCheck);
                        if (cellCheck.equals("Cell phone number successfully captured.")) break;
                    }
                    msg.setRecipient(recipient);

                    // Validate message length — must be under 250 characters
                    String messageText;
                    while (true) {
                        System.out.println("Please keep your message under 250 characters.");
                        System.out.print("Enter your message: ");
                        messageText = input.nextLine();
                        String lengthCheck = msg.checkMessageLength(messageText);
                        System.out.println(lengthCheck);
                        if (lengthCheck.equals("Message ready to send.")) break;
                    }
                    msg.setMessageText(messageText);

                    // Display the generated message ID and hash for reference
                    System.out.println("Message ID: " + msg.getMessageID());
                    System.out.println("Message Hash: " + msg.createMessageHash());
                    System.out.println(msg.sentMessage());

                    // Add the message to the sent list and increment the counter
                    sentMessages.add(msg);
                    messageCounter++;
                    break;

                case "2":
                    // Display all sent messages, or notify if none have been sent yet
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        System.out.println("\n--- SENT MESSAGES ---");
                        // Iterate through all sent messages and print recipient and content
                        for (Message m : sentMessages) {
                            System.out.println("To: " + m.getRecipient()
                                    + " | Message: " + m.getMessageText());
                        }
                    }
                    break;

                case "3":
                    // User chose to quit — close scanner and exit the application
                    System.out.println("Goodbye!");
                    input.close();
                    return;

                default:
                    // Handle any input that isn't 1, 2, or 3
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}