package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

public interface AuthorizationService {
    void validateAccessToUser(Long userId) throws ForbiddenAccessException, NotFoundUserException;
}
