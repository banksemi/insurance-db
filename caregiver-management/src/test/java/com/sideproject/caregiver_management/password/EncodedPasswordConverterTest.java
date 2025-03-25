package com.sideproject.caregiver_management.password;

import com.sideproject.caregiver_management.password.converter.EncodedPasswordConverter;
import com.sideproject.caregiver_management.password.dto.EncodedPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncodedPasswordConverterTest {
    private final EncodedPasswordConverter converter = new EncodedPasswordConverter();

    @Test
    @DisplayName("객체가 스트링으로 변환되는지 검증")
    void convertToDatabaseColumn() {
        EncodedPassword encodedPassword = new EncodedPassword("module", "password");
        String data = converter.convertToDatabaseColumn(encodedPassword);

        assertEquals("module:password", data);
    }

    @Test
    @DisplayName("비정상적인 값을 가진 객체는 변환할 수 없음")
    void convertToDatabaseColumn_InvalidEncodedPassword() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToDatabaseColumn(
                new EncodedPassword("module", "pass:word")
        ));
        assertThrows(IllegalArgumentException.class, () -> converter.convertToDatabaseColumn(
                new EncodedPassword("module", "password:")
        ));
    }

    @Test
    @DisplayName("string 데이터로부터 객체를 생성하는 로직 확인")
    void convertToEntityAttribute() {
        EncodedPassword encodedPassword = converter.convertToEntityAttribute("module:password");

        assertEquals("module", encodedPassword.getPrefix());
        assertEquals("password", encodedPassword.getValue());
    }

    @Test
    @DisplayName("비정상적 string 데이터는 변환할 수 없음")
    void convertToEntityAttribute_invalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("a:b:c"));
    }
}