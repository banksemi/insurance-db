package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.common.KnownException;

public class DuplicateResourceException extends KnownException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
