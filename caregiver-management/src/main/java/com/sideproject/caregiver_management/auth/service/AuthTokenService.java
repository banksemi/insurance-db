package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.NotFoundTokenException;
import com.sideproject.caregiver_management.user.entity.User;

import java.time.Instant;

public interface AuthTokenService {
    String generateAndSaveAccessToken(User user, Instant expireAt);
    String generateAndSaveRefreshToken(User user, Instant expiredAt);
    User getUserFromAccessToken(String accessToken) throws NotFoundTokenException;
    User getUserFromRefreshToken(String accessToken) throws NotFoundTokenException;
    void removeAccessToken(String accessToken);
    void removeRefreshToken(String refreshToken);
}
