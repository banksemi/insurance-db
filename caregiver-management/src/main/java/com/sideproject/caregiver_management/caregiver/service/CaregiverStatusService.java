package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;

public interface CaregiverStatusService {
    String getReadableStatus(Caregiver caregiver);
}
