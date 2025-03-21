package com.sideproject.caregiver_management.insurance.service;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.exception.DuplicateInsuranceException;
import com.sideproject.caregiver_management.insurance.exception.NotFoundInsuranceException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;

public interface InsuranceService {
    Long createInsurance(Long userId, InsuranceUpdateRequest updateRequest) throws NotFoundUserException, DuplicateInsuranceException;
    Insurance getInsurance(Long userId) throws NotFoundInsuranceException; // 사용자 ID로 조회할 수 있음.
    void updateInsurance(Long userId, InsuranceUpdateRequest updateRequest) throws NotFoundInsuranceException;
}
