package com.mycompany.chatapp1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MessageTest contains all unit tests for the Message class.
 * Part 2 tests remain unchanged below.
 * Part 3 tests are added at the bottom and cover arrays, longest message,
 * search by ID, search by recipient, delete by hash, and the message report.
 *
 * @author Student
 */
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

        private int option;

        public TestableMessage(int messageNumber, int option) {
            super(messageNumber);
            this.option = option;
        }

        /**
         * Overrides sentMessage() to return a predictable result
         * and also populates the static arrays so Part 3 tests work correctly.
         *
         * @return the outcome string matching the chosen option
         */
        @Override
        public String sentMessage() {
            String hash = createMessageHash();
            switch (option) {
                case 1:
                    // Simulate Send: populate sentMessages, hashes, IDs, and recipients
                    Message.getSentMessages().add(getMessageText());
                    Message.getMessageHashes().add(hash);
                    Message.getMessageIDs().add(getMessageID());
                    Message.getRecipientList().add(getRecipient());
                    return "Message successfully sent.";
                case 2:
                    return "Press 0 to delete the message.";
                case 3:
                    // Simulate Store: populate hashes, IDs, and recipients (not sentMessages)
                    Message.getMessageHashes().add(hash);
                    Message.getMessageIDs().add(getMessageID());
                    Message.getRecipientList().add(getRecipient());
                    return "Message successfully stored.";
                default:
                    return "Invalid option.";
            }
        }
    }

    // =====================================
    // SETUP METHOD
    // =====================================

    /**
     * Runs before each test to reset static arrays and initialize fresh Message objects.
     * Clearing the arrays ensures tests do not interfere with each other.
     */
    @Before
    public void setUp() {
        // Clear all static arrays before each test to prevent state leaking between tests
        Message.clearAllArrays();

        // Message 1 — valid recipient with +27 international code
        message1 = new Message(1);
        message1.setRecipient("+27718693002");
        message1.setMessageText("Hi Mike, can you join us for dinner tonight?");

        // Message 2 — invalid recipient missing the +27 international code
        message2 = new Message(2);
        message2.setRecipient("08575975889");
        message2.setMessageText("Hi Keegan, did you receive the payment?");
    }

    // =====================================
    // MESSAGE LENGTH TESTS (Part 2 - unchanged)
    // =====================================

    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {
        String text = "Hello";
        String result = message1.checkMessageLength(text);
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount() {
        String text = "A".repeat(260);
        String result = message1.checkMessageLength(text);
        assertEquals("Message exceeds 250 characters by 10, please reduce size.", result);
    }

    @Test
    public void testCheckMessageLength_exactlyAtLimit_returnsSuccess() {
        String text = "A".repeat(250);
        String result = message1.checkMessageLength(text);
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLength_oneOver_returnsFailureWithCountOf1() {
        String text = "A".repeat(251);
        String result = message1.checkMessageLength(text);
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", result);
    }

    // =====================================
    // RECIPIENT CELL TESTS (Part 2 - unchanged)
    // =====================================

    @Test
    public void testCheckRecipientCell_validNumber_returnsSuccess() {
        String result = message1.checkRecipientCell(message1.getRecipient());
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testCheckRecipientCell_invalidNumber_returnsFailure() {
        String result = message2.checkRecipientCell(message2.getRecipient());
        assertEquals("Cell phone number is incorrectly formatted or does not contain international code.", result);
    }

    // =====================================
    // MESSAGE HASH TESTS (Part 2 - unchanged)
    // =====================================

    @Test
    public void testCreateMessageHash_correctFormat_endsWithExpectedWords() {
        String hash = message1.createMessageHash();
        assertTrue(hash.endsWith(":1:HITONIGHT"));
    }

    @Test
    public void testCreateMessageHash_isUppercase() {
        String hash = message1.createMessageHash();
        assertEquals(hash.toUpperCase(), hash);
    }

    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {
        Message[] messages = {message1, message2};
        String[] expectedWords = {"HITONIGHT", "HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            String hash = messages[i].createMessageHash();
            assertTrue(hash.contains(expectedWords[i]));
        }
    }

    // =====================================
    // MESSAGE ID TESTS (Part 2 - unchanged)
    // =====================================

    @Test
    public void testCheckMessageID_generatedID_isNotNull() {
        assertNotNull("Message ID should not be null", message1.getMessageID());
    }

    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {
        assertTrue(message1.checkMessageID());
    }

    // =====================================
    // SENT MESSAGE TESTS (Part 2 - unchanged)
    // =====================================

    @Test
    public void testSentMessage_userSelectsSend_returnsCorrectString() {
        TestableMessage msg = new TestableMessage(1, 1);
        msg.setRecipient("+27834557896");
        msg.setMessageText("Did you get the cake?");
        String result = msg.sentMessage();
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {
        TestableMessage msg = new TestableMessage(1, 2);
        msg.setRecipient("+27718693002");
        msg.setMessageText("Test message.");
        String result = msg.sentMessage();
        assertEquals("Press 0 to delete the message.", result);
    }

    @Test
    public void testSentMessage_userSelectsStore_returnsCorrectString() {
        TestableMessage msg = new TestableMessage(1, 3);
        msg.setRecipient("+27718693002");
        msg.setMessageText("Test message.");
        String result = msg.sentMessage();
        assertEquals("Message successfully stored.", result);
    }

    // =====================================
    // PART 3 TESTS - NEW
    // =====================================

    /**
     * Test 1: Verifies that the sentMessages array is correctly populated
     * when messages 1 and 4 from the POE test data are flagged as Sent.
     * POE messages: "Did you get the cake?" to +27834557896
     *               "It is dinner time!" to 0838884567
     */
    @Test
    public void testSentMessagesArray_correctlyPopulated() {
        // Message 1 — Send
        TestableMessage msg1 = new TestableMessage(1, 1);
        msg1.setRecipient("+27834557896");
        msg1.setMessageText("Did you get the cake?");
        msg1.sentMessage();

        // Message 4 — Send (developer number, no international code)
        TestableMessage msg4 = new TestableMessage(4, 1);
        msg4.setRecipient("0838884567");
        msg4.setMessageText("It is dinner time!");
        msg4.sentMessage();

        // Both message texts must be present in the sentMessages array
        assertTrue(Message.getSentMessages().contains("Did you get the cake?"));
        assertTrue(Message.getSentMessages().contains("It is dinner time!"));
    }

    /**
     * Test 2: Verifies that displayLongestMessage() returns the correct message
     * from the storedMessages array using the POE test data.
     * Expected: "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessage_returnsCorrectMessage() {
        // Populate storedMessages directly using the POE test data
        Message.getStoredMessages().add("Hi, I am fine.");
        Message.getStoredMessages().add("Where are you? You are late! I have asked you to be on time.");
        Message.getStoredMessages().add("Ok, I am leaving without you.");

        Message helper = new Message(0);
        String result = helper.displayLongestMessage();

        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            result
        );
    }

    /**
     * Test 3: Verifies that searchByMessageID() returns the correct message
     * when searching for message 4's ID from the POE test data.
     * Expected: "It is dinner time!"
     */
    @Test
    public void testSearchByMessageID_returnsCorrectMessage() {
        // Simulate message 4 being sent — captures its auto-generated ID
        TestableMessage msg4 = new TestableMessage(4, 1);
        msg4.setRecipient("0838884567");
        msg4.setMessageText("It is dinner time!");
        msg4.sentMessage();

        // Retrieve the actual generated ID so we can search for it
        String generatedID = msg4.getMessageID();

        Message helper = new Message(0);
        String result = helper.searchByMessageID(generatedID);

        assertEquals("It is dinner time!", result);
    }

    /**
     * Test 4: Verifies that searchByRecipient() returns all messages
     * sent to +27838884567 (messages 2 and 5 from POE test data).
     * Expected: both message texts appear in the result.
     */
    @Test
    public void testSearchByRecipient_returnsAllMatchingMessages() {
        // Message 2 — sent to +27838884567
        TestableMessage msg2 = new TestableMessage(2, 1);
        msg2.setRecipient("+27838884567");
        msg2.setMessageText("Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage();

        // Message 5 — also sent to +27838884567
        TestableMessage msg5 = new TestableMessage(5, 1);
        msg5.setRecipient("+27838884567");
        msg5.setMessageText("Ok, I am leaving without you.");
        msg5.sentMessage();

        Message helper = new Message(0);
        String result = helper.searchByRecipient("+27838884567");

        // Both messages must appear in the result
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    /**
     * Test 5: Verifies that deleteByHash() removes message 2 from the arrays
     * and returns the correct success message.
     * Expected: "Message: Where are you? You are late! I have asked you to be on time. successfully deleted."
     */
    @Test
    public void testDeleteByHash_removesCorrectMessage() {
        // Send message 2 so it populates the arrays and generates a hash
        TestableMessage msg2 = new TestableMessage(2, 1);
        msg2.setRecipient("+27838884567");
        msg2.setMessageText("Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage();

        // Retrieve the actual hash that was generated and stored
        String generatedHash = Message.getMessageHashes().get(0);

        Message helper = new Message(0);
        String result = helper.deleteByHash(generatedHash);

        assertEquals(
            "Message: Where are you? You are late! I have asked you to be on time. successfully deleted.",
            result
        );
    }

    /**
     * Test 6: Verifies that printMessages() produces a report containing
     * the hash, recipient, and message text for all sent messages.
     */
    @Test
    public void testDisplayReport_containsRequiredFields() {
        // Send two messages to populate the arrays
        TestableMessage msg1 = new TestableMessage(1, 1);
        msg1.setRecipient("+27834557896");
        msg1.setMessageText("Did you get the cake?");
        msg1.sentMessage();

        TestableMessage msg2 = new TestableMessage(2, 1);
        msg2.setRecipient("+27838884567");
        msg2.setMessageText("Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage();

        String report = Message.printMessages();

        // Report must contain the hash, recipient, and message for each entry
        assertTrue(report.contains(Message.getMessageHashes().get(0)));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));

        assertTrue(report.contains(Message.getMessageHashes().get(1)));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
    }
}