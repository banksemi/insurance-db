package com.sideproject.caregiver_management.caregiver.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverCreateRequest {
    private String name;
    private Boolean isShared;
    private LocalDate birthday;
    private Integer genderCode;
    private LocalDate startDate;
}
