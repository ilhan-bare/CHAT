package com.mycompany.chatapp1;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
import org.json.JSONObject;

/**
 * Message class handles the creation, validation, sending, storage,
 * and retrieval of chat messages. Part 3 adds five static parallel arrays
 * for tracking all messages across the session, plus methods to search,
 * delete, and report on those messages.
 *
 * @author Student
 */
public class Message {

    // Unique 10-digit identifier generated automatically for each message
    private String messageID;

    // Tracks which message number this is in the session (e.g. 1st, 2nd message)
    private int messageNumber;

    // The recipient's cell phone number in +27 format
    private String recipient;

    // The actual text content of the message
    private String messageText;

    // =========================
    // PART 3 - FIVE STATIC PARALLEL ARRAYS
    // Static so they persist and are shared across all Message objects in the session
    // =========================

    /** Stores the text of every message the user chose to Send */
    private static List<String> sentMessages = new ArrayList<>();

    /** Stores the text of every message the user chose to Discard */
    private static List<String> disregardedMessages = new ArrayList<>();

    /** Stores messages loaded back from the JSON file (previously stored messages) */
    private static List<String> storedMessages = new ArrayList<>();

    /** Stores the hash string for every message that was sent or stored */
    private static List<String> messageHashes = new ArrayList<>();

    /** Stores the unique ID for every message that was sent or stored */
    private static List<String> messageIDs = new ArrayList<>();

    /** Parallel list tracking the recipient for each sent or stored message */
    private static List<String> recipientList = new ArrayList<>();

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Creates a new Message with the given message number
     * and immediately generates a unique message ID.
     *
     * @param messageNumber the position of this message in the session
     */
    public Message(int messageNumber) {
        this.messageNumber = messageNumber;
        generateMessageID();
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    /** Returns the auto-generated unique message ID */
    public String getMessageID() {
        return messageID;
    }

    /** Returns the message's position number in the session */
    public int getMessageNumber() {
        return messageNumber;
    }

    /** Returns the recipient's cell number */
    public String getRecipient() {
        return recipient;
    }

    /** Returns the text content of the message */
    public String getMessageText() {
        return messageText;
    }

    /** Sets the recipient's cell number after validation */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /** Sets the message text after length validation */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    // =========================
    // STATIC ARRAY ACCESSORS (used by unit tests)
    // =========================

    /** Returns the list of sent message texts */
    public static List<String> getSentMessages() {
        return sentMessages;
    }

    /** Returns the list of stored message texts loaded from JSON */
    public static List<String> getStoredMessages() {
        return storedMessages;
    }

    /** Returns the list of message hashes */
    public static List<String> getMessageHashes() {
        return messageHashes;
    }

    /** Returns the list of message IDs */
    public static List<String> getMessageIDs() {
        return messageIDs;
    }

    /** Returns the list of recipients parallel to sent/stored messages */
    public static List<String> getRecipientList() {
        return recipientList;
    }

    /**
     * Clears all static arrays. Used in unit tests to reset state between tests.
     */
    public static void clearAllArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
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
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        messageID = String.valueOf(number).substring(0, 10);
    }

    // =========================
    // CHECK MESSAGE ID
    // =========================

    /**
     * Validates that the message ID was generated and is within the allowed length.
     *
     * @return true if the ID is not null and is 10 characters or fewer
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // =========================
    // CHECK MESSAGE LENGTH
    // =========================

    /**
     * Checks whether the message text is within the 250-character limit.
     *
     * @param text the message text to validate
     * @return success message if valid, or failure message with overage count
     */
    public String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    // =========================
    // CHECK RECIPIENT CELL
    // =========================

    /**
     * Validates the recipient's cell number using a regex pattern.
     * Valid format: starts with +27 followed by exactly 9 digits.
     *
     * @param cell the cell number to validate
     * @return success or failure message
     */
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

    /**
     * Generates a hash string for the message in the format:
     * [first 2 chars of ID]:[message number]:[firstWord][lastWord]
     * Words are uppercased and stripped of punctuation.
     *
     * @return the formatted hash string
     */
    public String createMessageHash() {
        String[] words = messageText.toUpperCase().split(" ");
        String firstWord = words[0].replace(",", "").replace("?", "").replace(".", "").replace("!", "");
        String lastWord = words[words.length - 1].replace(",", "").replace("?", "").replace(".", "").replace("!", "");
        return messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    // =========================
    // SENT MESSAGE (UPDATED FOR PART 3)
    // =========================

    /**
     * Prompts the user to choose what to do with the composed message:
     * 1 = Send, 2 = Disregard, 3 = Store.
     * Part 3: also populates the relevant static arrays based on the choice.
     *
     * @return a string describing the outcome of the user's choice
     */
    public String sentMessage() {
        Scanner input = new Scanner(System.in);

        System.out.println("1) Send");
        System.out.println("2) Disregard");
        System.out.println("3) Store");
        System.out.print("Choose an option: ");
        String choice = input.nextLine().trim();

        String hash = createMessageHash();

        switch (choice) {
            case "1":
                // Populate sent, hash, ID, and recipient arrays
                sentMessages.add(messageText);
                messageHashes.add(hash);
                messageIDs.add(messageID);
                recipientList.add(recipient);
                return "Message successfully sent.";

            case "2":
                // Only the disregarded array is populated — no hash or ID tracking needed
                disregardedMessages.add(messageText);
                return "Press 0 to delete the message.";

            case "3":
                // Write to JSON file and populate hash, ID, and recipient arrays
                writeToJson();
                messageHashes.add(hash);
                messageIDs.add(messageID);
                recipientList.add(recipient);
                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    // =========================
    // WRITE TO JSON (from Part 2)
    // =========================

    /**
     * Writes this message to messages.json as a JSON object on a new line.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    private void writeToJson() {
        try (FileWriter writer = new FileWriter("messages.json", true)) {
            JSONObject obj = new JSONObject();
            obj.put("messageID", messageID);
            obj.put("recipient", recipient);
            obj.put("messageText", messageText);
            obj.put("messageHash", createMessageHash());
            writer.write(obj.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // =========================
    // PART 3 - LOAD STORED MESSAGES FROM JSON
    // =========================

    /**
     * Reads messages.json line by line and loads each stored message
     * into the storedMessages array. Called once at application startup
     * so that previously stored messages are available immediately.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    public static void loadStoredMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    JSONObject obj = new JSONObject(line);
                    storedMessages.add(obj.getString("messageText"));
                }
            }
            System.out.println(storedMessages.size() + " stored message(s) loaded from file.");
        } catch (IOException e) {
            // File may not exist yet on first run — this is expected, continue silently
            System.out.println("No stored messages file found. Starting fresh.");
        }
    }

    // =========================
    // PART 3 - DISPLAY LONGEST MESSAGE
    // =========================

    /**
     * Searches the storedMessages array and returns the message
     * with the greatest number of characters.
     *
     * @return the longest stored message, or a notice if no messages are stored
     */
    public String displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        String longest = "";
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    // =========================
    // PART 3 - SEARCH BY MESSAGE ID
    // =========================

    /**
     * Searches the messageIDs array for a match and returns the
     * corresponding message text using the same parallel index.
     *
     * @param id the message ID to search for
     * @return the matching message text, or a not-found message
     */
    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                // Use the same index to retrieve from sentMessages
                return sentMessages.get(i);
            }
        }
        return "Message not found.";
    }

    // =========================
    // PART 3 - SEARCH BY RECIPIENT
    // =========================

    /**
     * Searches for all messages sent to a given recipient number.
     * Returns all matches since more than one message may exist for the same recipient.
     *
     * @param recipient the recipient cell number to search for
     * @return all matching messages as a formatted string, or a not-found message
     */
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                results.append(sentMessages.get(i)).append("\n");
            }
        }
        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }

    // =========================
    // PART 3 - DELETE BY HASH
    // =========================

    /**
     * Finds a message by its hash and removes it from all parallel arrays.
     * Breaks immediately after deletion to avoid index shifting issues.
     *
     * @param hash the message hash to search for and delete
     * @return a success message with the deleted text, or a not-found message
     */
    public String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedText = sentMessages.get(i);
                // Remove from all parallel arrays at the same index
                messageHashes.remove(i);
                sentMessages.remove(i);
                messageIDs.remove(i);
                recipientList.remove(i);
                return "Message: " + deletedText + " successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    // =========================
    // PART 3 - DISPLAY MESSAGE REPORT (updated printMessages)
    // =========================

    /**
     * Builds and returns a formatted report of all sent messages.
     * Each entry shows the message hash, recipient, and message text.
     * Uses parallel array indexes to retrieve all three fields together.
     *
     * @return the full formatted report as a string
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("---------------------------\n");
            report.append("Hash:      ").append(messageHashes.get(i)).append("\n");
            report.append("Recipient: ").append(recipientList.get(i)).append("\n");
            report.append("Message:   ").append(sentMessages.get(i)).append("\n");
        }
        report.append("---------------------------");
        return report.toString();
    }
}
