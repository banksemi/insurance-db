package com.sideproject.caregiver_management.auth.service.password_algorithm;

public interface PasswordAlgorithm {
    String getPrefix();
    String encode(String rawPassword);
}
