package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CaregiverStatusServiceImpl implements CaregiverStatusService {
    private final Clock clock;

    @Override
    public String getReadableStatus(Caregiver caregiver) {
        LocalDate currentDate = LocalDate.now(clock);
        LocalDate startDate = caregiver.getStartDate();
        LocalDate endDate = caregiver.getEndDate();

        if (endDate != null) {
            if (startDate.equals(endDate)) {
                return "삭제됨";
            }
            if (!currentDate.isBefore(endDate)) {
                return "퇴사";
            }
        }
        if (currentDate.isBefore(startDate)) {
            return "근무예정";
        }
        return "근무";
    }
}
