package com.sideproject.caregiver_management.user.exception;

public class NotFoundTenantException extends RuntimeException {
    public NotFoundTenantException(String message) {
        super(message);
    }
}
