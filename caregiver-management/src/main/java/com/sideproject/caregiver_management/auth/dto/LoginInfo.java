package com.sideproject.caregiver_management.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginInfo {
    private final Long tenantId;
    private final Long userId;
}
