package com.sideproject.caregiver_management.caregiver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
public class CaregiverEstimateResponse {
    private LocalDate endDate;
    private Long contractDays;
    private Long insuranceAmount;
}
