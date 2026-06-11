package com.mycompany.chatapp1;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * MainApp is the entry point of the ChatApp application.
 * It handles user registration, login, and the main menu loop.
 * Part 3 adds menu option 4 (Stored Messages) and loads saved messages on startup.
 *
 * @author Student
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // =========================
        // USER REGISTRATION
        // =========================
        System.out.println("\n=== USER REGISTRATION ===");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        while (true) {
            System.out.print("Enter a username (must contain _ and be 5 chars or less): ");
            String username = input.nextLine();

            if (!login.checkUsername(username)) {
                System.out.println("Username not correct. Please try again.");
                continue;
            }
            System.out.println("Username successfully captured.");

            System.out.print("Enter a password (min 8 chars, 1 capital, 1 number, 1 special): ");
            String password = input.nextLine();

            if (!login.checkPasswordComplexity(password)) {
                System.out.println("Password not correct. Please try again.");
                continue;
            }
            System.out.println("Password successfully captured.");

            System.out.print("Enter phone number (+27...): ");
            String phone = input.nextLine();

            if (!login.checkCellPhoneNumber(phone)) {
                System.out.println("Phone not correct. Please try again.");
                continue;
            }
            System.out.println("Cell phone number successfully added.");

            String response = login.registerUser(username, password, phone);
            System.out.println(response);

            if (response.equals("User registered successfully.")) {
                System.out.println("Welcome " + firstName + " " + lastName + ", your account has been created.");
                break;
            }
        }

        // =========================
        // USER LOGIN
        // =========================
        System.out.println("\n=== USER LOGIN ===");

        System.out.print("Username: ");
        String loginUsername = input.nextLine();
        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loggedIn));

        if (!loggedIn) {
            System.out.println("Access denied. Exiting application.");
            input.close();
            return;
        }

        // =========================
        // PART 3 - LOAD STORED MESSAGES FROM JSON
        // Called right after login so the storedMessages array is ready before the menu appears
        // =========================
        Message.loadStoredMessages();

        // =========================
        // WELCOME MESSAGE
        // =========================
        System.out.println("\nWelcome to QuickChat.");

        // =========================
        // HOW MANY MESSAGES
        // =========================
        int maxMessages = 0;

        while (true) {
            System.out.print("How many messages would you like to send? ");
            String countInput = input.nextLine().trim();
            try {
                maxMessages = Integer.parseInt(countInput);
                if (maxMessages > 0) break;
                System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        ArrayList<Message> sentMessages = new ArrayList<>();
        int messageCounter = 1;

        // =========================
        // MAIN MENU LOOP
        // =========================
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    if (messageCounter > maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + ".");
                        break;
                    }

                    Message msg = new Message(messageCounter);

                    String recipient;
                    while (true) {
                        System.out.print("Enter recipient cell number (+27...): ");
                        recipient = input.nextLine().trim();
                        String cellCheck = msg.checkRecipientCell(recipient);
                        System.out.println(cellCheck);
                        if (cellCheck.equals("Cell phone number successfully captured.")) break;
                    }
                    msg.setRecipient(recipient);

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

                    System.out.println("Message ID: " + msg.getMessageID());
                    System.out.println("Message Hash: " + msg.createMessageHash());
                    System.out.println(msg.sentMessage());

                    sentMessages.add(msg);
                    messageCounter++;
                    break;

                case "2":
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        System.out.println("\n--- SENT MESSAGES ---");
                        for (Message m : sentMessages) {
                            System.out.println("To: " + m.getRecipient()
                                    + " | Message: " + m.getMessageText());
                        }
                    }
                    break;

                case "3":
                    System.out.println("Goodbye!");
                    input.close();
                    return;

                case "4":
                    // Open stored messages menu for option 4
                    storedMessagesMenu(input);
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    // =========================
    // PART 3 - STORED MESSAGES SUB-MENU
    // =========================

    /**
     * Displays the stored messages sub-menu and handles all six sub-options.
     * Each option delegates to the appropriate method in Message.java.
     * Keeps looping until the user chooses to return to the main menu.
     *
     * @param input the shared Scanner instance from main()
     */
    private static void storedMessagesMenu(Scanner input) {
        Message helper = new Message(0); // Helper instance to call instance methods

        while (true) {
            System.out.println("\n======================================");
            System.out.println("         STORED MESSAGES MENU");
            System.out.println("======================================");
            System.out.println("a) Display all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by ID");
            System.out.println("d) Search all messages for a recipient");
            System.out.println("e) Delete a message using its hash");
            System.out.println("f) Display full message report");
            System.out.println("g) Return to main menu");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {

                case "a":
                    // Display sender and recipient of all stored messages
                    if (Message.getStoredMessages().isEmpty()) {
                        System.out.println("No stored messages found.");
                    } else {
                        System.out.println("\n--- ALL STORED MESSAGES ---");
                        for (int i = 0; i < Message.getStoredMessages().size(); i++) {
                            System.out.println((i + 1) + ". " + Message.getStoredMessages().get(i));
                        }
                    }
                    break;

                case "b":
                    // Find and print the longest stored message
                    System.out.println("\nLongest message:");
                    System.out.println(helper.displayLongestMessage());
                    break;

                case "c":
                    // Search for a message by its unique ID
                    System.out.print("Enter message ID to search: ");
                    String searchID = input.nextLine().trim();
                    System.out.println(helper.searchByMessageID(searchID));
                    break;

                case "d":
                    // Search for all messages sent to a given recipient
                    System.out.print("Enter recipient number to search: ");
                    String searchRecipient = input.nextLine().trim();
                    System.out.println(helper.searchByRecipient(searchRecipient));
                    break;

                case "e":
                    // Delete a message by entering its hash
                    System.out.print("Enter message hash to delete: ");
                    String deleteHash = input.nextLine().trim();
                    System.out.println(helper.deleteByHash(deleteHash));
                    break;

                case "f":
                    // Display the full formatted report of all sent messages
                    System.out.println(Message.printMessages());
                    break;

                case "g":
                    // Return to the main menu
                    return;

                default:
                    System.out.println("Invalid option. Please enter a, b, c, d, e, f, or g.");
            }
        }
    }
}