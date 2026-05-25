package com.mycompany.chatapp1;

import java.util.Random;
import java.util.Scanner;

public class Message {

    // Unique 10-digit identifier generated automatically for each message
    private String messageID;

    // Tracks which message number this is in the session (e.g. 1st, 2nd message)
    private int messageNumber;

    // The recipient's cell phone number in +27 format
    private String recipient;

    // The actual text content of the message
    private String messageText;

    /**
     * Constructor — creates a new Message with the given message number
     * and immediately generates a unique message ID.
     */
    public Message(int messageNumber) {
        this.messageNumber = messageNumber;
        generateMessageID(); // Auto-generate ID on creation
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    // Returns the auto-generated unique message ID
    public String getMessageID() {
        return messageID;
    }

    // Returns the message's position number in the session
    public int getMessageNumber() {
        return messageNumber;
    }

    // Returns the recipient's cell number
    public String getRecipient() {
        return recipient;
    }

    // Returns the text content of the message
    public String getMessageText() {
        return messageText;
    }

    // Sets the recipient's cell number after validation in MainApp
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    // Sets the message text after length validation in MainApp
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    // =========================
    // GENERATE MESSAGE ID
    // =========================

    /**
     * Generates a random 10-digit numeric message ID.
     * Uses a number in the range 1,000,000,000 to 9,999,999,999
     * to ensure the result is always exactly 10 digits long.
     */
    private void generateMessageID() {
        Random random = new Random();

        // Generate a random 10-digit number starting from 1,000,000,000
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);

        // Convert to string and take the first 10 characters to ensure fixed length
        messageID = String.valueOf(number).substring(0, 10);
    }

    // =========================
    // CHECK MESSAGE ID
    // =========================

    /**
     * Validates that the message ID was generated and is within the allowed length.
     * Returns true if the ID is not null and is 10 characters or fewer.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // =========================
    // CHECK MESSAGE LENGTH
    // =========================

    /**
     * Checks whether the message text is within the 250-character limit.
     * If valid, returns a success message.
     * If too long, returns a failure message stating the exact overage.
     */
    public String checkMessageLength(String text) {
        if (text.length() <= 250) {
            // Message is within the allowed limit
            return "Message ready to send.";
        } else {
            // Calculate how many characters over the limit the message is
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by "
                    + excess
                    + ", please reduce size.";
        }
    }

    // =========================
    // CHECK RECIPIENT CELL
    // =========================

    /**
     * Validates the recipient's cell number using a regex pattern.
     * Valid format: starts with +27 followed by exactly 9 digits (12 chars total).
     * Returns a success or failure message accordingly.
     */
    public String checkRecipientCell(String cell) {
        // Regex: must start with +27 and be followed by exactly 9 digits
        if (cell.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain international code.";
        }
    }

    // =========================
    // CREATE MESSAGE HASH
    // =========================

    /**
     * Generates a hash string for the message in the format:
     * [first 2 chars of ID] : [message number] : [first word][last word]
     * The message text is converted to uppercase and punctuation is stripped
     * from the first and last words before building the hash.
     */
    public String createMessageHash() {
        // Split the message text into words (uppercase for consistency)
        String[] words = messageText.toUpperCase().split(" ");

        // Strip commas and question marks from the first word
        String firstWord = words[0].replace(",", "").replace("?", "");

        // Strip commas and question marks from the last word
        String lastWord = words[words.length - 1].replace(",", "").replace("?", "");

        // Build and return the hash: first 2 ID chars : message number : firstWord + lastWord
        return messageID.substring(0, 2)
                + ":"
                + messageNumber
                + ":"
                + firstWord
                + lastWord;
    }

    // =========================
    // SENT MESSAGE
    // =========================

    /**
     * Prompts the user to choose what to do with the composed message:
     * 1 = Send, 2 = Disregard, 3 = Store.
     * Returns a string describing the outcome of their choice.
     */
    public String sentMessage() {
        Scanner input = new Scanner(System.in);

        // Display the three available options to the user
        System.out.println("1) Send");
        System.out.println("2) Disregard");
        System.out.println("3) Store");
        System.out.print("Choose an option: ");

        // Read and trim the user's choice
        String choice = input.nextLine().trim();

        switch (choice) {
            case "1":
                // User chose to send the message
                return "Message successfully sent.";

            case "2":
                // User chose to disregard — prompt to delete
                return "Press 0 to delete the message.";

            case "3":
                // User chose to store the message for later
                return "Message successfully stored.";

            default:
                // Handle any input that isn't 1, 2, or 3
                return "Invalid option.";
        }
    }
}