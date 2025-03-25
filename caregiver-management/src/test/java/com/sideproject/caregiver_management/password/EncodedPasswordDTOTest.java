package com.sideproject.caregiver_management.password;

import com.sideproject.caregiver_management.password.converter.EncodedPasswordConverter;
import com.sideproject.caregiver_management.password.dto.EncodedPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncodedPasswordDTOTest {
    @Test
    @DisplayName("EncodedPassword의 equals 메서드 테스트")
    void testEquals_Success() {
        EncodedPassword password1 = new EncodedPassword("module", "password");
        EncodedPassword password2 = new EncodedPassword("module", "password");
        EncodedPassword password3 = new EncodedPassword("otherModule", "password");

        assertEquals(password1, password1);
        assertEquals(password1, password2);
        assertNotEquals(password1, password3);
    }

    @Test
    @DisplayName("EncodedPassword equals 메서드의 null 입력 확인")
    void testEquals_Null() {
        EncodedPassword password1 = new EncodedPassword(null, null);

        assertNotEquals(password1, null);
    }

    @Test
    @DisplayName("클래스가 다른 경우 equals 메서드가 false를 반환하는지 테스트")
    void testEquals_DifferentClass() {
        EncodedPassword password = new EncodedPassword("module", "password");
        String differentClassObject = "module:password";

        assertNotEquals(password, differentClassObject);
    }
}