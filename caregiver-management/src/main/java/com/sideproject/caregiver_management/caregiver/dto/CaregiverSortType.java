package com.sideproject.caregiver_management.caregiver.dto;

public enum CaregiverSortType {
    ID("id"),
    NAME("name"),
    MEMO("memo");

    private final String field;

    CaregiverSortType(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static CaregiverSortType from(String value) {
        for (CaregiverSortType sortType : CaregiverSortType.values()) {
            if (sortType.getField().equalsIgnoreCase(value)) {
                return sortType;
            }
        }
        throw new IllegalArgumentException("Invalid sort type: " + value);
    }
}
