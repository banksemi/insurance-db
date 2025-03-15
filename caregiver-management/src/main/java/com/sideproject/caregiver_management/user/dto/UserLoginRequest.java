package com.sideproject.caregiver_management.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "로그인 ID는 필수 항목입니다.")
    private String loginId;
    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
