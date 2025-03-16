package com.sideproject.caregiver_management.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty(message = "리프레쉬 토큰이 필요합니다.")
    private String refreshToken;
}
