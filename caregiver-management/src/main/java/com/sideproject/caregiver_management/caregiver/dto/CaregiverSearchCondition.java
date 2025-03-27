package com.sideproject.caregiver_management.caregiver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CaregiverSearchCondition {
    private Boolean isShared;
    private CaregiverSortType sortBy;
}
