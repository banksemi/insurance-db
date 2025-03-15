package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface TenantService {
    Long createTenant(String name);
    Long createUser(Long tenantId, UserCreateRequest userCreateRequest);

    Tenant findTenantById(Long id) throws NotFoundTenantException;
    User login(String loginId, String password)  throws NotFoundUserException, PasswordNotMatchException;
}
