package com.sideproject.caregiver_management.password.service;

import com.sideproject.caregiver_management.password.dto.EncodedPassword;

public interface PasswordService {
    EncodedPassword encode(String rawPassword);
    boolean matches(String inputPassword, EncodedPassword encodedPassword);
}
