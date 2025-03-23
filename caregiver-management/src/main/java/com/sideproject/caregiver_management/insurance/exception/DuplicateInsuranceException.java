package com.sideproject.caregiver_management.insurance.exception;

import com.sideproject.caregiver_management.common.exception.KnownException;

public class DuplicateInsuranceException extends KnownException {
    public DuplicateInsuranceException(String message) {
        super(message);
    }
}
