package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.service.TenantService;
import com.sideproject.caregiver_management.user.service.TenantServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AuthServiceImpl authService;

    @Test
    @Transactional
    void login() {
        Long tenentId = tenantService.createTenant("test");
        Long userId1 = tenantService.createUser(
                tenentId,
                UserCreateRequest
                        .builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        Long userId2 = tenantService.createUser(
                tenentId,
                UserCreateRequest
                        .builder()
                        .loginId("test2")
                        .password("test12")
                        .name("test2")
                        .build()
        );
        User login_user = authService.login("test", "test12");
        assertEquals(userId1, login_user.getId());

        User login_user2 = authService.login("test2", "test12");
        assertEquals(userId2, login_user2.getId());

        assertThrows(PasswordNotMatchException.class, () -> authService.login("test", "test1"));
        assertThrows(PasswordNotMatchException.class, () -> authService.login("test", ""));
        assertThrows(NullPointerException.class, () -> authService.login("test", null));
    }
}