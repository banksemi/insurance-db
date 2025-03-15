package com.sideproject.caregiver_management.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
// @Data를 쓰면 비밀번호가 ToString에 노출될 수 있음 주의!
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
// 필드 자체에 대한 제약 조건은 DTO에서 처리하고 비지니스 로직 제약은 서비스에서 처리해보자.
public class UserCreateRequest {
    @NotBlank(message = "로그인 ID는 필수 항목입니다.") // NotNull -> null, NotEmpty -> null, "", NotBlank = " " 까지
    @Size(min = 4, max = 30, message = "로그인 ID는 4자 이상 30자 이하로 입력해주세요.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;
}
