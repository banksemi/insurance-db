package com.sideproject.caregiver_management.password;

import com.sideproject.caregiver_management.password.dto.EncodedPassword;
import com.sideproject.caregiver_management.password.service.PasswordService;
import com.sideproject.caregiver_management.password.service.algorithm.SHA256PasswordAlgorithm;
import com.sideproject.caregiver_management.password.service.PasswordServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceImplTest {
    private final SHA256PasswordAlgorithm sha256Algorithm = new SHA256PasswordAlgorithm();
    private final DummyPasswordAlgorithm dummyAlgorithm = new DummyPasswordAlgorithm();
    private final PasswordService passwordService = new PasswordServiceImpl(
            sha256Algorithm,
            List.of(sha256Algorithm, dummyAlgorithm)
    );

    @Test
    @DisplayName("비밀번호 서비스의 encode 함수는 미리 지정된 알고리즘 1개만 사용해야함")
    void encode() {
        EncodedPassword data = passwordService.encode("test12");

        // then
        assertEquals(sha256Algorithm.encode("test12"), data.getValue());
        assertEquals("sha256", data.getPrefix());
    }

    @Test
    @DisplayName("암호화된 비밀번호의 알고리즘을 식별하고 해당 알고리즘으로 비밀번호를 체크하는지 검증")
    void matches() {
        // given
        EncodedPassword sha256Data = new EncodedPassword("sha256", sha256Algorithm.encode("test12"));
        EncodedPassword dummyData = new EncodedPassword("dummy", dummyAlgorithm.encode("test12"));
        assertEquals(dummyData.getValue(), "dummytest12");

        // when, then
        // EncodedPassword 객체에 명시된 알고리즘을 사용하여 비밀번호를 검증해야함.
        assertTrue(passwordService.matches("test12", sha256Data));
        assertTrue(passwordService.matches("test12", dummyData));

        // 일치하지 않는 비밀번호에 대해서도 예외 처리가 되어야함.
        assertFalse(passwordService.matches("test123", sha256Data));
        assertFalse(passwordService.matches("test123", dummyData));
    }
}