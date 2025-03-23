package com.sideproject.caregiver_management.caregiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverCreateRequest {
    private String name;
    private Boolean isShared;
    private LocalDate birthday;
    private Integer gender;
    private LocalDate startDate;
}
