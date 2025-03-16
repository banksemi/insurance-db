package com.sideproject.caregiver_management.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;
    private final Long expireAt;
}
