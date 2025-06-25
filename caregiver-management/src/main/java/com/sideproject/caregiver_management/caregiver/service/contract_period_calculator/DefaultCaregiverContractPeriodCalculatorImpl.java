package com.sideproject.caregiver_management.caregiver.service.contract_period_calculator;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class DefaultCaregiverContractPeriodCalculatorImpl implements CaregiverContractPeriodCalculator {
    @Override
    public Long getContractDays(Caregiver caregiver) {
        Insurance insurance = caregiver.getInsurance();
        return ChronoUnit.DAYS.between(caregiver.getStartDate(), insurance.getEndDate());
    }

    @Override
    public Optional<Long> getEffectiveDays(Caregiver caregiver) {
        LocalDate endDate = caregiver.getEndDate();
        if (endDate == null)
            return Optional.empty();
        else
            return Optional.of(ChronoUnit.DAYS.between(caregiver.getStartDate(), caregiver.getEndDate()));
    }
}
