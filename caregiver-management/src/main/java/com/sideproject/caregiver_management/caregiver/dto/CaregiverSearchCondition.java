package com.sideproject.caregiver_management.caregiver.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverSearchCondition {
    private Boolean isShared;
    private CaregiverSortType sortBy;
}
