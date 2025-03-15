package com.sideproject.caregiver_management.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class APIExceptionResponse {
    private int status;
    private List<Map<String, Object>> errors;

}
