package com.sideproject.caregiver_management.caregiver.service.mapper;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverResponse;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.CaregiverStatusService;
import com.sideproject.caregiver_management.caregiver.service.contract_period_calculator.CaregiverContractPeriodCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaregiverMapperImpl implements CaregiverMapper {
    private final CaregiverContractPeriodCalculator contractPeriodCalculator;
    private final CaregiverStatusService statusService;

    @Override
    public CaregiverResponse toDTO(Caregiver caregiver) {
        return CaregiverResponse.builder()
                .id(caregiver.getId())
                .name(caregiver.getName())
                .isShared(caregiver.getIsShared())
                .birthday(caregiver.getBirthday())
                .genderCode(caregiver.getGenderCode())
                .startDate(caregiver.getStartDate())
                .endDate(caregiver.getEndDate())
                .memo(caregiver.getMemo())
                .insuranceAmount(caregiver.getInsuranceAmount())
                .refundAmount(caregiver.getRefundAmount())
                .contractDays(contractPeriodCalculator.getContractDays(caregiver))
                .effectiveDays(contractPeriodCalculator.getEffectiveDays(caregiver).orElse(null))
                .createdAt(caregiver.getCreatedAt())
                .isApproved(caregiver.getIsApproved())
                .status(statusService.getReadableStatus(caregiver))
                .build();
    }

    @Override
    public List<CaregiverResponse> toDTO(List<Caregiver> caregivers) {
        if (caregivers == null)
            return List.of();
        return caregivers.stream().map(this::toDTO).toList(); // 자바 16 이상
    }
}
