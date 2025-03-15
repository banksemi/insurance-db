package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.auth.exception.NotFoundUserException;

public interface AuthService {
    User login(String loginId, String password) throws NotFoundUserException, PasswordNotMatchException;
}
