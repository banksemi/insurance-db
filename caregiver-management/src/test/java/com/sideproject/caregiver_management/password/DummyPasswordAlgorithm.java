package com.sideproject.caregiver_management.password;

import com.sideproject.caregiver_management.password.service.algorithm.PasswordAlgorithm;

public class DummyPasswordAlgorithm implements PasswordAlgorithm {
    @Override
    public String getPrefix() {
        return "dummy";
    }

    @Override
    public String encode(String rawPassword) {
        return "dummy" + rawPassword;
    }
}
