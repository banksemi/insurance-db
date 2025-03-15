package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.exception.DuplicateResourceException;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;


public interface TenantService {
    Long createTenant(String name) throws DuplicateResourceException;
    Long createUser(Long tenantId, UserCreateRequest userCreateRequest) throws DuplicateResourceException;

    Tenant findTenantById(Long id) throws NotFoundTenantException;
}
