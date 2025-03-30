package com.sideproject.caregiver_management.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class UserResponse {
    private final Long userId;
    private final String loginId;
    private final String name;

    private final Boolean isAdmin;
}
