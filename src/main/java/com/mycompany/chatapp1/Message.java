package com.mycompany.chatapp1;

import java.util.Random;
import java.util.Scanner;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;

    // Constructor
    public Message(int messageNumber) {
        this.messageNumber = messageNumber;
        generateMessageID();
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    // =========================
    // GENERATE MESSAGE ID
    // =========================

    private void generateMessageID() {
        Random random = new Random();
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        messageID = String.valueOf(number).substring(0, 10);
    }

    // =========================
    // CHECK MESSAGE ID
    // =========================

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // =========================
    // CHECK MESSAGE LENGTH
    // =========================

    public String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by "
                    + excess
                    + ", please reduce size.";
        }
    }

    // =========================
    // CHECK RECIPIENT CELL
    // =========================

    public String checkRecipientCell(String cell) {
        if (cell.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain international code.";
        }
    }

    // =========================
    // CREATE MESSAGE HASH
    // =========================

    public String createMessageHash() {
        String[] words = messageText.toUpperCase().split(" ");
        String firstWord = words[0].replace(",", "").replace("?", "");
        String lastWord = words[words.length - 1].replace(",", "").replace("?", "");
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

    public String sentMessage() {
        Scanner input = new Scanner(System.in);
        System.out.println("1) Send");
        System.out.println("2) Disregard");
        System.out.println("3) Store");
        System.out.print("Choose an option: ");
        String choice = input.nextLine().trim();

        switch (choice) {
            case "1":
                return "Message successfully sent.";
            case "2":
                return "Press 0 to delete the message.";
            case "3":
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }
}