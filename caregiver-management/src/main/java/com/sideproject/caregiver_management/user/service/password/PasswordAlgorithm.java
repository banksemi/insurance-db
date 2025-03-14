package com.sideproject.caregiver_management.user.service.password;

public interface PasswordAlgorithm {
    String getPrefix();
    String encode(String rawPassword);
}
