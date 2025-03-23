package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSearchCondition;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CaregiverServiceImpl implements CaregiverService {
    @Override
    public List<Caregiver> getCaregivers(Long insuranceId, CaregiverSearchCondition searchCondition) {
        return List.of();
    }

    @Override
    public Caregiver getCaregiver(Long caregiverId) {
        return null;
    }

    @Override
    public Long addCaregiver(Long insuranceId, CaregiverCreateRequest request) {
        return 0L;
    }

    @Override
    public Long requestEndDate(Long caregiverId, LocalDate endDate) {
        return 0L;
    }

    @Override
    public void updateMemo(Long caregiverId, String memo) {

    }
}
