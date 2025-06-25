package com.sideproject.caregiver_management.caregiver.service.amount;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;

import java.util.Optional;

public interface CaregiverInsuranceAmountCalculator {
    Long getInsuranceAmount(Caregiver caregiver);
    Long getRefundAmount(Caregiver caregiver);
}
