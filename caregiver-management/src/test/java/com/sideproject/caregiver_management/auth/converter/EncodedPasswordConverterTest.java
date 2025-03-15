package com.sideproject.caregiver_management.auth.converter;

import com.sideproject.caregiver_management.auth.dto.EncodedPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncodedPasswordConverterTest {

    @Test
    void convertToDatabaseColumn() {
        EncodedPasswordConverter converter = new EncodedPasswordConverter();
        EncodedPassword encodedPassword = new EncodedPassword("module", "password");

        String data = converter.convertToDatabaseColumn(encodedPassword);

        assertEquals("module:password", data);
    }

    @Test
    void convertToDatabaseColumn_InvalidEncodedPassword() {
        EncodedPasswordConverter converter = new EncodedPasswordConverter();

        assertThrows(IllegalArgumentException.class, () -> converter.convertToDatabaseColumn(
                new EncodedPassword("module", "pass:word")
        ));
        assertThrows(IllegalArgumentException.class, () -> converter.convertToDatabaseColumn(
                new EncodedPassword("module", "password:")
        ));
    }

    @Test
    void convertToEntityAttribute() {
        EncodedPasswordConverter converter = new EncodedPasswordConverter();
        String data = "module:password";

        EncodedPassword encodedPassword = converter.convertToEntityAttribute(data);

        assertEquals("module", encodedPassword.getPrefix());
        assertEquals("password", encodedPassword.getValue());
    }

    @Test
    void convertToEntityAttribute_invalidFormat() {
        EncodedPasswordConverter converter = new EncodedPasswordConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("a:b:c"));
    }
}