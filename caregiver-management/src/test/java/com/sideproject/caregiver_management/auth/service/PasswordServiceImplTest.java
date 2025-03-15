package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.dto.EncodedPassword;
import com.sideproject.caregiver_management.auth.service.password_algorithm.SHA256PasswordAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceImplTest {
    @Test
    void encode() {
        SHA256PasswordAlgorithm sha256PasswordAlgorithm = new SHA256PasswordAlgorithm();
        PasswordServiceImpl passwordService = new PasswordServiceImpl(sha256PasswordAlgorithm, List.of(sha256PasswordAlgorithm));

        EncodedPassword data = passwordService.encode("test12");

        assertEquals(sha256PasswordAlgorithm.encode("test12"), data.getValue());
        assertEquals("sha256", data.getPrefix());
    }

    @Test
    void matches() {
        SHA256PasswordAlgorithm sha256PasswordAlgorithm = new SHA256PasswordAlgorithm();
        PasswordServiceImpl passwordService = new PasswordServiceImpl(sha256PasswordAlgorithm, List.of(sha256PasswordAlgorithm));

        EncodedPassword data = passwordService.encode("test12");

        assertTrue(passwordService.matches("test12", data));
        assertFalse(passwordService.matches("test123", data));
    }
}