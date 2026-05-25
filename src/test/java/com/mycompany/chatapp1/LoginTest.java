package com.mycompany.chatapp1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    // object
    Login login = new Login();

    //========== USERNAME TEST ==========//

    @Test
    public void testValidUsername() {
        assertTrue(login.checkUsername("kyl_1"));
    }

    @Test
    public void testInvalidUsername() {
        assertFalse(login.checkUsername("kyle!!!!!!"));
    }

    //========== PASSWORD TEST ==========//

    @Test
    public void testValidPassword() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    //========== PHONE TEST ==========//

    @Test
    public void testValidPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testInvalidPhoneNumber() {
        assertFalse(login.checkCellPhoneNumber("083898976"));
    }

    //========== LOGIN TEST ==========//

    @Test
    public void testLoginSuccess() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFail() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "wrong"));
    }
}