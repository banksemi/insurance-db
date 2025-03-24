package com.sideproject.caregiver_management.caregiver.service.amount;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.insurance.entity.Insurance;

import java.time.LocalDate;
import java.util.Optional;

public interface CaregiverInsuranceAmountCalculator {
    Long getContractDays(Caregiver caregiver);
    Long getInsuranceAmount(Caregiver caregiver);
    Long getRefundAmount(Caregiver caregiver);
}
