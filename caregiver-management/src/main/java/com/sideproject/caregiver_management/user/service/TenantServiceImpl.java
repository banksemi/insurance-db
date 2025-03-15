package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.repository.TenantRepository;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

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
    @Transactional
    public Long createUser(Long tenantId, UserCreateRequest userCreateRequest) {
        Tenant tenant = tenantRepository.findOne(tenantId);
        String encodedPassword = passwordService.encode(userCreateRequest.getPassword());
        User user = User.builder()
                .tenant(tenant)
                .loginId(userCreateRequest.getLoginId())
                .password(encodedPassword)
                .name(userCreateRequest.getName())
                .build();
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Tenant findTenantById(Long id) throws NotFoundTenantException {
        Tenant tenant = tenantRepository.findOne(id);

        if (tenant == null)
            throw new NotFoundTenantException("테넌트를 찾을 수 없습니다.");

        return tenant;
    }

    @Override
    public User login(String loginId, String password) throws NotFoundUserException, PasswordNotMatchException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            throw new NotFoundUserException("사용자를 찾을 수 없습니다.");
        }

        if (!passwordService.matches(password, user.get().getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        return user.get();
    }
}
