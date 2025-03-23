package com.sideproject.caregiver_management.caregiver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaregiverSearchCondition {
    private Boolean isShared;
    private CaregiverSortType sortBy;
}
