package com.sideproject.caregiver_management.caregiver.service.mapper;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverResponse;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;

import java.util.List;

public interface CaregiverMapper {
    CaregiverResponse toDTO(Caregiver caregiver);
    List<CaregiverResponse> toDTO(List<Caregiver> caregivers);
}
