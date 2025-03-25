package com.sideproject.caregiver_management.password.converter;

import com.sideproject.caregiver_management.password.dto.EncodedPassword;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EncodedPasswordConverter implements AttributeConverter<EncodedPassword, String> {

    @Override
    public String convertToDatabaseColumn(EncodedPassword encodedPassword) {
        if (encodedPassword == null || encodedPassword.getValue().contains(":"))
            throw new IllegalArgumentException("Invalid encoded password");
        return encodedPassword.getPrefix() + ":" + encodedPassword.getValue();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(String s) {
        if (s == null)
            throw new IllegalArgumentException("Encoded password cannot be null");

        String[] split = s.split(":");
        if (split.length != 2)
            throw new IllegalArgumentException("Invalid encoded password format");

        return new EncodedPassword(split[0], split[1]);
    }
}
