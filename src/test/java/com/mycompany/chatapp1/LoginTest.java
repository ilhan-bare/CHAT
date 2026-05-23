package com.mycompany.chatapp1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
class LoginTest {

    // object
    Login login = new Login();

    //========== USERNAME TEST ==========//

    @Test
    void testValidUsername() {
        assertTrue(login.checkUsername("kyl_1"));
    }

    @Test
    void testInvalidUsername() {
        assertFalse(login.checkUsername("kyle!!!!!!"));
    }

    //========== PASSWORD TEST ==========//

    @Test
    void testValidPassword() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testInvalidPassword() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    //========== PHONE TEST ==========//

    @Test
    void testValidPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testInvalidPhoneNumber() {
        assertFalse(login.checkCellPhoneNumber("083898976"));
    }

    //========== LOGIN TEST ==========//

    @Test
    void testLoginSuccess() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFail() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "wrong"));
    }
}