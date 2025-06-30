package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;

public interface AuthorizationService {
    void validateAccessToUser(Long userId) throws ForbiddenAccessException, NotFoundUserException;

    void validateAdmin() throws ForbiddenAccessException;
}
