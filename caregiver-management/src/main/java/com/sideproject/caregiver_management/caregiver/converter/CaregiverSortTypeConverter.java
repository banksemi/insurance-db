package com.sideproject.caregiver_management.caregiver.converter;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverSortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CaregiverSortTypeConverter implements Converter<String, CaregiverSortType> {
    @Override
    public CaregiverSortType convert(String source) {
        return CaregiverSortType.from(source);
    }
}