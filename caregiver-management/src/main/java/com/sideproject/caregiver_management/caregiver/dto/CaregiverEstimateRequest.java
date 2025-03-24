package com.sideproject.caregiver_management.caregiver.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaregiverEstimateRequest {
    @NotNull(message = "보험 타입은 필수 항목입니다.")
    private Boolean isShared;

    @NotNull(message = "보험 시작일은 필수 항목입니다.")
    private LocalDate startDate;
}
