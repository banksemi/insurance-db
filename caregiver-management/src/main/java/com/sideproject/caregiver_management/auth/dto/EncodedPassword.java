package com.sideproject.caregiver_management.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EncodedPassword {
    private final String prefix;
    private final String value;
}
