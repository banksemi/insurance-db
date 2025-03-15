package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.LoginIdNotMatchException;
import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.entity.User;

public interface AuthService {
    LoginResponse login(String loginId, String password) throws LoginIdNotMatchException, PasswordNotMatchException;
}
