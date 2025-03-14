package com.sideproject.caregiver_management.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
// @Data를 쓰면 비밀번호가 ToString에 노출될 수 있음 주의!
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCreateRequest {
    private String loginId;
    private String password;
    private String name;
}
