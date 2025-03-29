package com.sideproject.caregiver_management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ListResponse<T> {
    private int count;
    private List<T> data;
}
