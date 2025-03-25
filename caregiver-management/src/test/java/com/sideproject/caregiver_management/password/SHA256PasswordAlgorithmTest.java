package com.sideproject.caregiver_management.password;

import com.sideproject.caregiver_management.password.service.algorithm.SHA256PasswordAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA256PasswordAlgorithmTest {
    private final SHA256PasswordAlgorithm passwordAlgorithm = new SHA256PasswordAlgorithm();

    @Test
    @DisplayName("PHP 서비스에서 사용중이던 비밀번호 해시값(SHA256)을 호환할 수 있는지 검증")
    void encode() {
        // given
        String originalPassword = "test12";

        // when
        String encodedPassword = passwordAlgorithm.encode(originalPassword);

        // then
        assertEquals("a98ec5c5044800c88e862f007b98d89815fc40ca155d6ce7909530d792e909ce", encodedPassword);
    }

    @Test
    @DisplayName("띄어쓰기 한칸도 다른 비밀번호로 인식되어야함")
    void encode_withSpace() {
        String password1 = passwordAlgorithm.encode("test12");
        String password2 = passwordAlgorithm.encode("test12 ");

        assertNotEquals(password1, password2);
    }
}