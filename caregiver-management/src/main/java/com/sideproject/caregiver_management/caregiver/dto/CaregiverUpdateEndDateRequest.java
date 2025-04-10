package com.sideproject.caregiver_management.caregiver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor // request 입력을 받기 위해 기본 생성자 필요
@AllArgsConstructor
public class CaregiverUpdateEndDateRequest {
    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate endDate;
}
