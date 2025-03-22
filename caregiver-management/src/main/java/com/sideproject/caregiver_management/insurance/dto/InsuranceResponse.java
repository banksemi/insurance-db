package com.sideproject.caregiver_management.insurance.dto;

import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class InsuranceResponse {
    private LocalDate startDate;
    private LocalDate endDate;

    private String policyNumber;

    private Long personalInsuranceFee;
    private Long sharedInsuranceFee;

}
