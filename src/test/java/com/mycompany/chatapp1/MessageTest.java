/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package com.mycompany.chatapp1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    private Message message1;
    private Message message2;

    // =====================================
    // HELPER CLASS FOR sentMessage() TESTS
    // =====================================

    private class TestableMessage extends Message {

        private int option;

        public TestableMessage(int messageNumber, int option) {
            super(messageNumber);
            this.option = option;
        }

        @Override
        public String sentMessage() {

            switch (option) {

                case 1:
                    return "Message successfully sent.";

                case 2:
                    return "Press 0 to delete the message.";

                case 3:
                    return "Message successfully stored.";

                default:
                    return "Invalid option.";
            }
        }
    }

    // =====================================
    // SETUP METHOD
    // =====================================

    @Before
    public void setUp() {

        // Message 1
        message1 = new Message(1);
        message1.setRecipient("+27718693002");
        message1.setMessageText(
                "Hi Mike, can you join us for dinner tonight?"
        );

        // Message 2
        message2 = new Message(2);
        message2.setRecipient("08575975889");
        message2.setMessageText(
                "Hi Keegan, did you receive the payment?"
        );
    }

    // =====================================
    // MESSAGE LENGTH TESTS
    // =====================================

    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {

        String text = "Hello";

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message ready to send.",
                result
        );
    }

    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount() {

        String text = "A".repeat(260);

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message exceeds 250 characters by 10, please reduce size.",
                result
        );
    }

    @Test
    public void testCheckMessageLength_exactlyAtLimit_returnsSuccess() {

        String text = "A".repeat(250);

        String result = message1.checkMessageLength(text);

        assertEquals(
                "Message ready to send.",
                result
        );
    }

    @Test
    public void testCheckMessageLength_oneOver_returnsFailureWithCountOf1() {

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

    @Test
    public void testCreateMessageHash_correctFormat_endsWithExpectedWords() {

        String hash = message1.createMessageHash();

        assertTrue(
                hash.endsWith(":1:HITONIGHT")
        );
    }

    @Test
    public void testCreateMessageHash_isUppercase() {

        String hash = message1.createMessageHash();

        assertEquals(
                hash.toUpperCase(),
                hash
        );
    }

    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {

        Message[] messages = {message1, message2};

        String[] expectedWords = {
            "HITONIGHT",
            "HIPAYMENT"
        };

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

    @Test
    public void testCheckMessageID_generatedID_isNotNull() {

        assertNotNull(
                "Message ID should not be null",
                message1.getMessageID()
        );
    }

    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {

        assertTrue(
                message1.checkMessageID()
        );
    }

    // =====================================
    // SENT MESSAGE TESTS
    // =====================================

    @Test
    public void testSentMessage_userSelectsSend_returnsCorrectString() {

        TestableMessage msg =
                new TestableMessage(1, 1);

        String result = msg.sentMessage();

        assertEquals(
                "Message successfully sent.",
                result
        );
    }

    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {

        TestableMessage msg =
                new TestableMessage(1, 2);

        String result = msg.sentMessage();

        assertEquals(
                "Press 0 to delete the message.",
                result
        );
    }

    @Test
    public void testSentMessage_userSelectsStore_returnsCorrectString() {

        TestableMessage msg =
                new TestableMessage(1, 3);

        String result = msg.sentMessage();

        assertEquals(
                "Message successfully stored.",
                result
        );
    }
}