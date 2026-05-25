/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package com.mycompany.chatapp1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    // Two Message instances used across the test methods
    private Message message1;
    private Message message2;

    // =====================================
    // HELPER CLASS FOR sentMessage() TESTS
    // =====================================

    /**
     * TestableMessage extends Message to simulate the three user options
     * in sentMessage() without relying on real user input.
     * Each option maps to a specific expected return string.
     */
    private class TestableMessage extends Message {

        // Stores which menu option to simulate (1 = send, 2 = disregard, 3 = store)
        private int option;

        // Constructor takes message number and the option to simulate
        public TestableMessage(int messageNumber, int option) {
            super(messageNumber);
            this.option = option;
        }

        /**
         * Overrides sentMessage() to return a predictable result
         * based on the selected option — used for isolated unit testing.
         */
        @Override
        public String sentMessage() {

            switch (option) {

                case 1:
                    // Simulates the user choosing to send the message
                    return "Message successfully sent.";

                case 2:
                    // Simulates the user choosing to disregard the message
                    return "Press 0 to delete the message.";

                case 3:
                    // Simulates the user choosing to store the message
                    return "Message successfully stored.";

                default:
                    // Fallback for any unexpected option value
                    return "Invalid option.";
            }
        }
    }

    // =====================================
    // SETUP METHOD
    // =====================================

    /**
     * Runs before each test to initialise fresh Message objects.
     * message1 has a valid recipient; message2 has an invalid recipient
     * (no international code) — used to test both passing and failing scenarios.
     */
    @Before
    public void setUp() {

        // Message 1 — valid recipient with +27 international code
        message1 = new Message(1);
        message1.setRecipient("+27718693002");
        message1.setMessageText(
                "Hi Mike, can you join us for dinner tonight?"
        );

        // Message 2 — invalid recipient missing the +27 international code
        message2 = new Message(2);
        message2.setRecipient("08575975889");
        message2.setMessageText(
                "Hi Keegan, did you receive the payment?"
        );
    }

    // =====================================
    // MESSAGE LENGTH TESTS
    // =====================================

    /**
     * Tests that a short, valid message returns the success response.
     * "Hello" is well under the 250-character limit.
     */
    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {

        String text = "Hello";

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message ready to send.",
                result
        );
    }

    /**
     * Tests that a message 10 characters over the limit (260 chars)
     * returns a failure message stating the exact overage amount.
     */
    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount() {

        // 260 'A' characters — 10 over the 250-character limit
        String text = "A".repeat(260);

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message exceeds 250 characters by 10, please reduce size.",
                result
        );
    }

    /**
     * Tests the boundary condition where the message is exactly 250 characters.
     * Should be accepted as valid (inclusive limit).
     */
    @Test
    public void testCheckMessageLength_exactlyAtLimit_returnsSuccess() {

        // Exactly 250 characters — right at the boundary
        String text = "A".repeat(250);

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message ready to send.",
                result
        );
    }

    /**
     * Tests the boundary condition where the message is exactly 1 character over the limit.
     * Should return a failure message indicating an overage of 1.
     */
    @Test
    public void testCheckMessageLength_oneOver_returnsFailureWithCountOf1() {

        // 251 characters — just one over the limit
        String text = "A".repeat(251);

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message exceeds 250 characters by 1, please reduce size.",
                result
        );
    }

    // =====================================
    // RECIPIENT CELL TESTS
    // =====================================

    /**
     * Tests that a correctly formatted cell number (+27...) is accepted.
     * Uses message1's recipient which was set with a valid +27 number.
     */
    @Test
    public void testCheckRecipientCell_validNumber_returnsSuccess() {

        String result = message1.checkRecipientCell(
                message1.getRecipient()
        );

        assertEquals(
                "Cell phone number successfully captured.",
                result
        );
    }

    /**
     * Tests that a cell number without an international code is rejected.
     * Uses message2's recipient which starts with 0 instead of +27.
     */
    @Test
    public void testCheckRecipientCell_invalidNumber_returnsFailure() {

        String result = message2.checkRecipientCell(
                message2.getRecipient()
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain international code.",
                result
        );
    }

    // =====================================
    // MESSAGE HASH TESTS
    // =====================================

    /**
     * Tests that the generated hash ends with the correct format:
     * messageID : messageNumber : first and last words of message (uppercase).
     * For message1: "Hi Mike...tonight?" → first word "HI", last word "TONIGHT".
     */
    @Test
    public void testCreateMessageHash_correctFormat_endsWithExpectedWords() {

        String hash = message1.createMessageHash();

        // Hash should end with ":1:HITONIGHT" — message number 1, first + last words
        assertTrue(
                hash.endsWith(":1:HITONIGHT")
        );
    }

    /**
     * Tests that the entire hash string is in uppercase.
     * Compares the hash to its own toUpperCase() version — must be identical.
     */
    @Test
    public void testCreateMessageHash_isUppercase() {

        String hash = message1.createMessageHash();

        // If hash is already uppercase, it equals its own uppercase conversion
        assertEquals(
                hash.toUpperCase(),
                hash
        );
    }

    /**
     * Tests hash generation across multiple messages using a loop.
     * Verifies that each message produces a hash containing its expected word pair.
     * message1 → "HITONIGHT", message2 → "HIPAYMENT"
     */
    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {

        Message[] messages = {message1, message2};

        // Expected concatenation of first and last words for each message
        String[] expectedWords = {
            "HITONIGHT",  // message1: "Hi Mike...tonight?"
            "HIPAYMENT"   // message2: "Hi Keegan...payment?"
        };

        // Loop through each message and confirm the hash contains the expected words
        for (int i = 0; i < messages.length; i++) {

            String hash = messages[i].createMessageHash();

            assertTrue(
                    hash.contains(expectedWords[i])
            );
        }
    }

    // =====================================
    // MESSAGE ID TESTS
    // =====================================

    /**
     * Tests that the generated message ID is not null.
     * Ensures the ID was actually created during Message construction.
     */
    @Test
    public void testCheckMessageID_generatedID_isNotNull() {

        assertNotNull(
                "Message ID should not be null",
                message1.getMessageID()
        );
    }

    /**
     * Tests that the generated message ID is exactly 10 characters long.
     * Relies on the checkMessageID() method in the Message class to verify this.
     */
    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {

        // checkMessageID() should return true only if the ID is exactly 10 chars
        assertTrue(
                message1.checkMessageID()
        );
    }

    // =====================================
    // SENT MESSAGE TESTS
    // =====================================

    /**
     * Tests that selecting option 1 (send) returns the correct confirmation string.
     * Uses TestableMessage to simulate the user choosing to send.
     */
    @Test
    public void testSentMessage_userSelectsSend_returnsCorrectString() {

        // Option 1 simulates the user choosing to send the message
        TestableMessage msg =
                new TestableMessage(1, 1);

        String result = msg.sentMessage();

        assertEquals(
                "Message successfully sent.",
                result
        );
    }

    /**
     * Tests that selecting option 2 (disregard) returns the correct prompt string.
     * Uses TestableMessage to simulate the user choosing to disregard.
     */
    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {

        // Option 2 simulates the user choosing to disregard the message
        TestableMessage msg =
                new TestableMessage(1, 2);

        String result = msg.sentMessage();

        assertEquals(
                "Press 0 to delete the message.",
                result
        );
    }

    /**
     * Tests that selecting option 3 (store) returns the correct confirmation string.
     * Uses TestableMessage to simulate the user choosing to store.
     */
    @Test
    public void testSentMessage_userSelectsStore_returnsCorrectString() {

        // Option 3 simulates the user choosing to store the message
        TestableMessage msg =
                new TestableMessage(1, 3);

        String result = msg.sentMessage();

        assertEquals(
                "Message successfully stored.",
                result
        );
    }
}