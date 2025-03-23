package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverRequest;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaregiverServiceImpl implements CaregiverService {
    @Override
    public List<Caregiver> getCaregivers(Long insuranceId) {
        return List.of();
    }

    @Override
    public Caregiver getCaregiver(Long id) {
        return null;
    }

    @Override
    public Long createCaregiver(Long insuranceId, CaregiverRequest request) {
        return 0L;
    }

    @Override
    public Long updateCaregiver(Long caregiverId, CaregiverRequest request) {
        return 0L;
    }

    @Override
    public void updateMemo(Long caregiverId, String memo) {

    }

    @Override
    public void approveCaregiver(Long caregiverId) {

    }

    @Override
    public Long createCaregiverAsApproved(Long insuranceId, CaregiverRequest request) {
        return 0L;
    }

    @Override
    public Long updateCaregiverAsApproved(Long caregiverId, CaregiverRequest request) {
        return 0L;
    }
}
