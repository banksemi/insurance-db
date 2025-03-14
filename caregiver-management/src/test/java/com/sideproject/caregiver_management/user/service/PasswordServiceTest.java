package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.service.password.PasswordAlgorithm;
import com.sideproject.caregiver_management.user.service.password.SHA256PasswordAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PasswordServiceTest {
    @Test
    void oldPasswordTest() {
        SHA256PasswordAlgorithm passwordAlgorithm = new SHA256PasswordAlgorithm();
        PasswordService passwordService = new PasswordServiceImpl(
                passwordAlgorithm, List.of(passwordAlgorithm)
        );

        String rawPassword = "test12";
        String encodedPassword = "sha256:a98ec5c5044800c88e862f007b98d89815fc40ca155d6ce7909530d792e909ce";

        assertEquals(encodedPassword, passwordService.encode(rawPassword));
        assertTrue(passwordService.matches(rawPassword, encodedPassword));
        assertFalse(passwordService.matches("a123", encodedPassword));
        assertFalse(passwordService.matches("", encodedPassword));
    }
}