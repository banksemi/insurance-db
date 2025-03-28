package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverDuplicateException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateBeforeNowException;
import com.sideproject.caregiver_management.caregiver.exception.NotFoundCaregiverException;
import com.sideproject.caregiver_management.caregiver.repository.CaregiverRepository;
import com.sideproject.caregiver_management.caregiver.service.amount.CaregiverInsuranceAmountCalculator;
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
    private final CaregiverRepository caregiverRepository;


    @Override
    public List<Caregiver> getCaregivers(Insurance insurance, CaregiverSearchCondition searchCondition) {
        if (searchCondition.getSortBy() == CaregiverSortType.MEMO) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return caregiverRepository.findAllByInsuranceId(insurance.getId(), searchCondition);
    }

    @Override
    public Caregiver getCaregiver(Long caregiverId) throws NotFoundCaregiverException {
        Caregiver caregiver = caregiverRepository.findById(caregiverId);
        if (caregiver == null) {
            throw new NotFoundCaregiverException();
        }
        return caregiver;
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
    public void requestEndDate(Long caregiverId, LocalDate endDate) {
        Caregiver caregiver = getCaregiver(caregiverId);  // throw NotFoundCaregiverException
        caregiver.setDate(CaregiverDateUpdate.ofEndDate(endDate));
        caregiver.calculateAmounts(calculator);
    }

    @Override
    public void updateMemo(Long caregiverId, String memo) {

    }

    @Override
    @Transactional(readOnly = true)
    public CaregiverEstimateResponse getCaregiverEstimate(Insurance insurance, CaregiverEstimateRequest request) {
        Caregiver dummyCaregiver = Caregiver.builder()
                .insurance(insurance)
                .isShared(request.getIsShared())
                .build();

        dummyCaregiver.setDate(CaregiverDateUpdate.ofStartDate(request.getStartDate()));

        return CaregiverEstimateResponse.builder()
                .contractDays(calculator.getContractDays(dummyCaregiver))
                .endDate(insurance.getEndDate()) // 해지일 미지정 상태이므로 보험의 만기일을 사용
                .insuranceAmount(calculator.getInsuranceAmount(dummyCaregiver))
                .build();
    }
}
