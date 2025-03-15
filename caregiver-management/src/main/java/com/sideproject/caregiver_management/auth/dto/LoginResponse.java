package com.sideproject.caregiver_management.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class LoginResponse {
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;
    private final Long expireAt;
}
