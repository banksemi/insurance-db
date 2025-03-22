package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.auth.service.PasswordService;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.DuplicateResourceException;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.repository.TenantRepository;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createTenant(String name) throws DuplicateResourceException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant name must not be null or empty");
        }

        Tenant tenant = new Tenant();
        tenant.setName(name);

        try {
            tenantRepository.save(tenant);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("테넌트가 이미 존재합니다.");
        }
        return tenant.getId();
    }

    @Override
    @Transactional
    public Long createUser(Long tenantId, UserCreateRequest userCreateRequest) throws DuplicateResourceException {
        Tenant tenant = tenantRepository.findOne(tenantId);
        User user = User.builder()
                .tenant(tenant)
                .loginId(userCreateRequest.getLoginId())
                .password(passwordService.encode(userCreateRequest.getPassword()))
                .name(userCreateRequest.getName())
                .build();
        try{
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("사용자가 이미 존재합니다.");
        }
        return user.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Tenant findTenantById(Long id) throws NotFoundTenantException {
        Tenant tenant = tenantRepository.findOne(id);

        if (tenant == null)
            throw new NotFoundTenantException("테넌트를 찾을 수 없습니다.");

        return tenant;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) throws NotFoundTenantException {
        User user = userRepository.findOne(id);

        if (user == null)
            throw new NotFoundUserException();

        return user;
    }
}
