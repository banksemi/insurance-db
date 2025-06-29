package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.dto.LoginInfo;
import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.auth.exception.LoginIdNotMatchException;
import com.sideproject.caregiver_management.auth.exception.NeedAuthenticationException;
import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;

import java.util.Optional;

public interface AuthService {
    LoginResponse login(String loginId, String password) throws LoginIdNotMatchException, PasswordNotMatchException;
    Optional<LoginInfo> validateAccessToken(String accessToken) throws NeedAuthenticationException, ForbiddenAccessException;
    LoginResponse refreshAccessToken(String refreshToken) throws NeedAuthenticationException;
}
