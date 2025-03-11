package com.sideproject.caregiver_management.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
class SHA256PasswordServiceTest {
    private SHA256PasswordService passwordService = new SHA256PasswordService();

    @Test
    void old_password_test() {
        String rawPassword = "test12";
        String encodedPassword = "a98ec5c5044800c88e862f007b98d89815fc40ca155d6ce7909530d792e909ce";

        assertEquals(encodedPassword, passwordService.encode(rawPassword));

        assertTrue(passwordService.matches(rawPassword, encodedPassword));
        assertFalse(passwordService.matches(rawPassword, "a123"));
    }
}