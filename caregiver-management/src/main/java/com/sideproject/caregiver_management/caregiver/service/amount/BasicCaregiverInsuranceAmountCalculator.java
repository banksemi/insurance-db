package com.sideproject.caregiver_management.caregiver.service.amount;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class BasicCaregiverInsuranceAmountCalculator implements CaregiverInsuranceAmountCalculator {
    private boolean checkRevoked(Caregiver caregiver) {
        return caregiver.getEndDate() != null && caregiver.getEndDate().isEqual(caregiver.getStartDate());
    }

    @Override
    public Long getContractDays(Caregiver caregiver) {
        Insurance insurance = caregiver.getInsurance();
        return ChronoUnit.DAYS.between(caregiver.getStartDate(), insurance.getEndDate());
    }

    @Override
    public Long getInsuranceAmount(Caregiver caregiver) {
        if (checkRevoked(caregiver))  // 즉시 해지한 경우 보험료 0원
            return 0L;

        Insurance insurance = caregiver.getInsurance();
        long baseAmount = caregiver.getIsShared() ? insurance.getSharedInsuranceFee() : insurance.getPersonalInsuranceFee();

        // 보험 금액은 항상 병원 보험의 종료일을 기준으로 계산
        return Math.round((double)baseAmount * getContractDays(caregiver) / insurance.getDays());
    }

    @Override
    public Long getRefundAmount(Caregiver caregiver) {
        if (caregiver.getEndDate() == null)
            return null;

        if (checkRevoked(caregiver))  // 즉시 해지한 경우 보험료 0원
            return 0L;

        // 사용하지 않은 날짜에 대해서는 환불액 측정
        // (반올림 처리되어있는) 청구된 보험료 기준으로 계산해야함
        long unusedDays = ChronoUnit.DAYS.between(caregiver.getEndDate(), caregiver.getInsurance().getEndDate());
        return Math.round((double) getInsuranceAmount(caregiver) * unusedDays / getContractDays(caregiver));
    }
}
