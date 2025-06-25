package com.sideproject.caregiver_management.caregiver.service.amount;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.contract_period_calculator.CaregiverContractPeriodCalculator;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicCaregiverInsuranceAmountCalculator implements CaregiverInsuranceAmountCalculator {
    private final CaregiverContractPeriodCalculator contractPeriodCalculator;

    private boolean checkRevoked(Caregiver caregiver) {
        return caregiver.getEndDate() != null && caregiver.getEndDate().isEqual(caregiver.getStartDate());
    }

    @Override
    public Long getInsuranceAmount(Caregiver caregiver) {
        if (checkRevoked(caregiver))  // 즉시 해지한 경우 보험료 0원
            return 0L;

        Insurance insurance = caregiver.getInsurance();
        long baseAmount = caregiver.getIsShared() ? insurance.getSharedInsuranceFee() : insurance.getPersonalInsuranceFee();

        Long contractDays = contractPeriodCalculator.getContractDays(caregiver);
        // 보험 금액은 항상 병원 보험의 종료일을 기준으로 계산
        return Math.round((double)baseAmount * contractDays / insurance.getDays());
    }

    @Override
    public Long getRefundAmount(Caregiver caregiver) {
        if (caregiver.getEndDate() == null)
            return null;

        if (checkRevoked(caregiver))  // 즉시 해지한 경우 보험료 0원
            return 0L;

        Long contractDays = contractPeriodCalculator.getContractDays(caregiver);
        Optional<Long> effectiveDays = contractPeriodCalculator.getEffectiveDays(caregiver);

        if (effectiveDays.isEmpty()) {
            // 예측할 수 없는 케이스, endDate가 있으므로 effectiveDays를 계산할 수 있어야하지만, 값을 받을 수 없는 상태.
            throw new RuntimeException("종료 날짜가 존재하지만 간병인의 실제 계약일을 추정할 수 없음");
        }
        // 사용하지 않은 날짜에 대해서는 환불액 측정
        // (반올림 처리되어있는) 청구된 보험료 기준으로 계산해야함
        long insuranceAmount = getInsuranceAmount(caregiver);
        long usedAmount = Math.round(
                (double) insuranceAmount
                        * effectiveDays.get()
                        / contractDays
        );

        return insuranceAmount - usedAmount;
    }
}
