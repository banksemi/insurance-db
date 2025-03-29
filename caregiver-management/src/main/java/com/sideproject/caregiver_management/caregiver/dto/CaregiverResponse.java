package com.sideproject.caregiver_management.caregiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CaregiverResponse {
    private Long id;
    private String name;

    private LocalDate birthday;
    private Integer genderCode;

    private Boolean isShared;

    private LocalDate startDate;
    private LocalDate endDate;
}
