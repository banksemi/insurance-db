package com.sideproject.caregiver_management.insurance.service;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.exception.DuplicateInsuranceException;
import com.sideproject.caregiver_management.insurance.exception.NotFoundInsuranceException;
import com.sideproject.caregiver_management.insurance.repository.InsuranceRepository;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final TenantService tenantService;
    private final InsuranceRepository insuranceRepository;

    @Override
    @Transactional
    public Long createInsurance(Long userId, InsuranceUpdateRequest updateRequest) throws NotFoundUserException, DuplicateInsuranceException {
        if (insuranceRepository.findByUserID(userId).isPresent()) {
            throw new DuplicateInsuranceException("이미 보험 정보가 생성되어있습니다.");
        }

        User user = tenantService.findUserById(userId); // throws NotFoundUserException
        Insurance insurance = new Insurance(user);
        insurance.updateInsurance(updateRequest);
        insuranceRepository.save(insurance);
        return insurance.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getInsuranceByUserId(Long userId) throws NotFoundInsuranceException {
        Optional<Insurance> insurance = insuranceRepository.findByUserID(userId);
        if (insurance.isPresent()) {
            return insurance.get();
        } else {
            throw new NotFoundInsuranceException("보험 정보를 찾을 수 없습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getInsuranceById(Long id) throws NotFoundInsuranceException {
        Optional<Insurance> insurance = insuranceRepository.findById(id);
        if (insurance.isPresent()) {
            return insurance.get();
        } else {
            throw new NotFoundInsuranceException("보험 정보를 찾을 수 없습니다.");
        }
    }

    @Override
    @Transactional
    public void updateInsurance(Long userId, InsuranceUpdateRequest updateRequest) throws NotFoundInsuranceException {
        Optional<Insurance> insurance = insuranceRepository.findByUserID(userId);
        if (insurance.isEmpty())
            throw new NotFoundInsuranceException("보험 정보를 찾을 수 없습니다.");
        insurance.get().updateInsurance(updateRequest);
    }
}
