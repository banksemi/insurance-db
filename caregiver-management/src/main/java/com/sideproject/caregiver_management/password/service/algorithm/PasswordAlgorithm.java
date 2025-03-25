package com.sideproject.caregiver_management.password.service.algorithm;

public interface PasswordAlgorithm {
    String getPrefix();
    String encode(String rawPassword);
}
