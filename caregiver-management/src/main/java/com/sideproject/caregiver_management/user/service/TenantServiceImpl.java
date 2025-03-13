package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public Long createTenant(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant name must not be null or empty");
        }

        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenantRepository.save(tenant);
        return tenant.getId();
    }

    @Override
    public Long createUser(long tenantId, UserCreateRequest userCreateRequest) {
        User user = User.builder().build();
        return user.getId();
    }
}
