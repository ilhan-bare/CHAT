package com.mycompany.chatapp1;

import java.util.Scanner;
import java.util.ArrayList;

public class MainApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // ========================= 
        // USER REGISTRATION
        // =========================
        System.out.println("\n=== USER REGISTRATION ===");
        while (true) {
            System.out.print("Enter a username: ");
            String username = input.nextLine();
            System.out.print("Enter a password: ");
            String password = input.nextLine();
            System.out.print("Enter phone (+27...): ");
            String phone = input.nextLine();

            String response = login.registerUser(username, password, phone);
            System.out.println(response);

            if (response.equals("User registered successfully.")) {
                break;
            }
            System.out.println("Please try again.\n");
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

        // Gate: only continue if login succeeded
        if (!loggedIn) {
            System.out.println("Access denied. Exiting application.");
            input.close();
            return;
        }

        // =========================
        // WELCOME MESSAGE (Req 2)
        // =========================
        System.out.println("\nWelcome to QuickChat.");

        // =========================
        // HOW MANY MESSAGES (Req 5)
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
        // MAIN MENU LOOP (Req 3 & 4)
        // =========================
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    // =========================
                    // SEND MESSAGES (Req 1 & 5)
                    // =========================
                    if (messageCounter > maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + ".");
                        break;
                    }

                    Message msg = new Message(messageCounter);

                    // Recipient
                    String recipient;
                    while (true) {
                        System.out.print("Enter recipient cell number (+27...): ");
                        recipient = input.nextLine().trim();
                        String cellCheck = msg.checkRecipientCell(recipient);
                        System.out.println(cellCheck);
                        if (cellCheck.equals("Cell phone number successfully captured.")) break;
                    }
                    msg.setRecipient(recipient);

                    // Message text
                    String messageText;
                    while (true) {
                        System.out.print("Enter your message: ");
                        messageText = input.nextLine();
                        String lengthCheck = msg.checkMessageLength(messageText);
                        System.out.println(lengthCheck);
                        if (lengthCheck.equals("Message ready to send.")) break;
                    }
                    msg.setMessageText(messageText);

                    // Confirm and send
                    System.out.println("Message ID: " + msg.getMessageID());
                    System.out.println("Message Hash: " + msg.createMessageHash());
                    System.out.println(msg.sentMessage());

                    sentMessages.add(msg);
                    messageCounter++;
                    break;

                case "2":
                    // =========================
                    // COMING SOON (Req 3b)
                    // =========================
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    // =========================
                    // QUIT (Req 3c & 4)
                    // =========================
                    System.out.println("Goodbye!");
                    input.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}