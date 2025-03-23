package com.sideproject.caregiver_management.caregiver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class CaregiverDateUpdate {
    private boolean updateStartDate = false;
    private LocalDate startDate;

    private boolean updateEndDate = false;
    private LocalDate endDate;

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.updateStartDate = true;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.updateEndDate = true;
    }
}
