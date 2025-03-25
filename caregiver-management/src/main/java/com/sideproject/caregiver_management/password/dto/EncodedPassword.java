package com.sideproject.caregiver_management.password.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class EncodedPassword {
    private final String prefix;
    private final String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncodedPassword that = (EncodedPassword) o;
        return Objects.equals(prefix, that.prefix) && Objects.equals(value, that.value);
    }
}
