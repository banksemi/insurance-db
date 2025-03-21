package com.sideproject.caregiver_management.insurance.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceUpdateRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String policyNumber;
    private Long personalInsuranceFee;
    private Long sharedInsuranceFee;
}
