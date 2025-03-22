package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.LoginSession;
import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorizationServiceImplTest {
    @Autowired
    private TenantService tenantService;
    private LoginSession loginSession;

    private AuthorizationServiceImpl authorizationService;

    private Long tenantId1;
    private Long tenantId2;
    private Long userId1;
    private Long adminUserId1;
    private Long userId2;

    @BeforeEach
    void setUp() {
        tenantId1 = tenantService.createTenant("t1");
        userId1 = tenantService.createUser(tenantId1,
                UserCreateRequest.builder()
                        .loginId("loginId1")
                        .password("password1")
                        .name("name1")
                        .build()
        );
        adminUserId1 = tenantService.createUser(tenantId1,
                UserCreateRequest.builder()
                        .loginId("loginId2")
                        .password("password1")
                        .name("name1")
                        .build()
        );
        tenantId2 = tenantService.createTenant("t2");
        userId2 = tenantService.createUser(tenantId2,
                UserCreateRequest.builder()
                        .loginId("loginId3")
                        .password("password1")
                        .name("name1")
                        .build()
        );

        tenantService.findUserById(adminUserId1).setIsAdmin(true);
        loginSession = Mockito.mock(LoginSession.class);
        authorizationService = new AuthorizationServiceImpl(loginSession, tenantService);
    }

    @Test
    void validateAccessToUser() {
        // 본인 리소스에 대한 접근
        Mockito.when(loginSession.getUserId()).thenReturn(userId1);
        Mockito.when(loginSession.getTenantId()).thenReturn(tenantId1);

        authorizationService.validateAccessToUser(userId1);
    }

    @Test
    void validateAccessToUser_OtherUser_InSameTenant() {
        // 같은 테넌트에 대한 다른 사용자 접근 (일반 유저 권한)
        Mockito.when(loginSession.getUserId()).thenReturn(userId1);
        Mockito.when(loginSession.getTenantId()).thenReturn(tenantId1);

        assertThrows(ForbiddenAccessException.class, ()->authorizationService.validateAccessToUser(adminUserId1));

        // 어드민 권한
        Mockito.when(loginSession.getUserId()).thenReturn(adminUserId1);
        authorizationService.validateAccessToUser(userId1);
    }

    @Test
    void validateAccessToUser_InOtherTenant() {
        // 다른 테넌트에 대한 어드민 접근 시도 -> 실패
        Mockito.when(loginSession.getUserId()).thenReturn(adminUserId1);
        Mockito.when(loginSession.getTenantId()).thenReturn(tenantId1);

        assertThrows(NotFoundUserException.class, ()->authorizationService.validateAccessToUser(userId2));
    }
}