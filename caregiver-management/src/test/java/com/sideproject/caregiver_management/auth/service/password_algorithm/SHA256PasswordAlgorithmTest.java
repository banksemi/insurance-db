package com.sideproject.caregiver_management.auth.service.password_algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA256PasswordAlgorithmTest {

    @Test
    void encode() {
        // given
        SHA256PasswordAlgorithm passwordAlgorithm = new SHA256PasswordAlgorithm();

        // when
        String encodedPassword = passwordAlgorithm.encode("test12");

        // then
        assertEquals(
                "a98ec5c5044800c88e862f007b98d89815fc40ca155d6ce7909530d792e909ce",
                encodedPassword
        );
    }
}