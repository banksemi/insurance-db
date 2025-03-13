package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.entity.User;

public interface AuthService {
    boolean login(String loginId, String password);
}
