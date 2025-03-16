package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.auth.exception.LoginIdNotMatchException;
import com.sideproject.caregiver_management.auth.exception.NeedAuthenticationException;
import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.entity.User;

import java.util.Optional;

public interface AuthService {
    LoginResponse login(String loginId, String password) throws LoginIdNotMatchException, PasswordNotMatchException;
    Optional<User> validateAccessToken(String accessToken, Auth.Role role) throws NeedAuthenticationException, ForbiddenAccessException;
}
