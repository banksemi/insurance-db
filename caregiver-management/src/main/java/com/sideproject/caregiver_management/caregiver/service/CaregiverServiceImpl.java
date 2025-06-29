package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.*;
import com.sideproject.caregiver_management.caregiver.repository.CaregiverRepository;
import com.sideproject.caregiver_management.caregiver.service.amount.CaregiverInsuranceAmountCalculator;
import com.sideproject.caregiver_management.caregiver.service.contract_period_calculator.CaregiverContractPeriodCalculator;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaregiverServiceImpl implements CaregiverService {
    private final Clock clock;
    private final CaregiverInsuranceAmountCalculator calculator;
    private final CaregiverContractPeriodCalculator contractPeriodCalculator;
    private final CaregiverRepository caregiverRepository;
    private final CaregiverStatusService statusService;

    private CaregiverResponse toDto(Caregiver caregiver) {
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

    private Caregiver getCaregiverEntity(Long caregiverId) throws NotFoundCaregiverException {
        Caregiver caregiver = caregiverRepository.findById(caregiverId);
        if (caregiver == null) {
            throw new NotFoundCaregiverException();
        }
        return caregiver;
    }

    @Override
    @Transactional(readOnly = true)
    public ListResponse<CaregiverResponse> getCaregivers(Insurance insurance, CaregiverSearchCondition searchCondition) {
        List<Caregiver> caregivers = caregiverRepository.findAllByInsuranceId(insurance.getId(), searchCondition);

        List<CaregiverResponse> dtoList = caregivers.stream().map(this::toDto).toList(); // 자바 16 이상
        // caregivers.forEach(caregiver -> dtoList.add(toDto(caregiver)));

        return ListResponse.<CaregiverResponse>builder()
                .count(dtoList.size())
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CaregiverResponse getCaregiver(Insurance insurance, Long caregiverId) throws NotFoundCaregiverException {
        Caregiver caregiver = getCaregiverEntity(caregiverId);

        if (!caregiver.getInsurance().equals(insurance))
            throw new CaregiverForbiddenException();

        return toDto(caregiver);
    }

    @Override
    @Transactional
    public Long addCaregiver(Insurance insurance, CaregiverCreateRequest request) {
        if (request.getStartDate().isBefore(LocalDate.now(clock)))
            throw new CaregiverStartDateBeforeNowException();

        Caregiver caregiver = new Caregiver(insurance, request.getIsShared());
        caregiver.setBirthday(request.getBirthday());
        caregiver.setGenderCode(request.getGenderCode());
        caregiver.setName(request.getName());
        caregiver.setIsApproved(false);

        caregiver.setDate(CaregiverDateUpdate.ofStartDate(request.getStartDate()));

        caregiver.calculateAmounts(calculator);

        if (caregiverRepository.isDuplicatedCaregiver(insurance.getId(), request))
            throw new CaregiverDuplicateException();

        caregiverRepository.save(caregiver);
        // 기존 등록
        return caregiver.getId();
    }

    @Override
    public void requestEndDate(Insurance insurance, Long caregiverId, LocalDate endDate, Boolean checkApproved) throws CaregiverForbiddenException, CaregiverNotApprovedException {
        Caregiver caregiver = getCaregiverEntity(caregiverId);  // throw NotFoundCaregiverException

        if (!caregiver.getInsurance().equals(insurance))
            throw new CaregiverForbiddenException();

        // 보험 종료일을 지정하기 위해서는 승인(확인)이 완료된 항목에 대해서만 요청할 수 있음.
        if (checkApproved && !caregiver.getIsApproved())
            throw new CaregiverNotApprovedException();

        caregiver.setDate(CaregiverDateUpdate.ofEndDate(endDate));
        caregiver.calculateAmounts(calculator);
        caregiver.setIsApproved(false);
    }

    @Override
    @Transactional
    public void updateMemo(Insurance insurance, Long caregiverId, String memo) {
        Caregiver caregiver = getCaregiverEntity(caregiverId); // NotFoundCaregiverException

        if (!caregiver.getInsurance().equals(insurance))
            throw new CaregiverForbiddenException();

        caregiver.setMemo(memo);
    }

    @Override
    @Transactional(readOnly = true)
    public CaregiverEstimateResponse getCaregiverEstimate(Insurance insurance, CaregiverEstimateRequest request) {
        Caregiver dummyCaregiver = Caregiver.builder()
                .insurance(insurance)
                .isShared(request.getIsShared())
                .build();

        dummyCaregiver.setDate(CaregiverDateUpdate.ofStartDate(request.getStartDate()));

        Long contractDays = contractPeriodCalculator.getContractDays(dummyCaregiver);
        return CaregiverEstimateResponse.builder()
                .contractDays(contractDays)
                .endDate(insurance.getEndDate()) // 해지일 미지정 상태이므로 보험의 만기일을 사용
                .insuranceAmount(calculator.getInsuranceAmount(dummyCaregiver))
                .build();
    }
}
