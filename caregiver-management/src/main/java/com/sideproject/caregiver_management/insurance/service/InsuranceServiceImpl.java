package com.sideproject.caregiver_management.insurance.service;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.exception.DuplicateInsuranceException;
import com.sideproject.caregiver_management.insurance.exception.NotFoundInsuranceException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Override
    public void createInsurance(Long userId, InsuranceUpdateRequest updateRequest) throws NotFoundUserException, DuplicateInsuranceException {

    }

    @Override
    public Insurance getInsurance(Long userId) throws NotFoundInsuranceException {
        return null;
    }

    @Override
    public void updateInsurance(Long userId, InsuranceUpdateRequest updateRequest) {

    }
}
