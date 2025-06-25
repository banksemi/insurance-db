package com.sideproject.caregiver_management.caregiver.service.contract_period_calculator;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;

import java.util.Optional;

public interface CaregiverContractPeriodCalculator {
    Long getContractDays(Caregiver caregiver);
    Optional<Long> getEffectiveDays(Caregiver caregiver);
}
