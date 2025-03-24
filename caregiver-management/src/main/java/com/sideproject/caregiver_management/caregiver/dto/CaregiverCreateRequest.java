package com.sideproject.caregiver_management.caregiver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverCreateRequest {
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "보험 정보는 필수 항목입니다.")
    private Boolean isShared;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    private LocalDate birthday;

    @NotNull(message = "성별 정보는 필수 항목입니다.")
    private Integer genderCode;

    @NotNull(message = "보험 시작일은 필수 항목입니다.")
    private LocalDate startDate;
}
