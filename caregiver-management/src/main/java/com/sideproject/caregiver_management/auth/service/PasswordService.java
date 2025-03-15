package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.dto.EncodedPassword;

public interface PasswordService {
    EncodedPassword encode(String rawPassword);
    boolean matches(String inputPassword, EncodedPassword encodedPassword);
}
