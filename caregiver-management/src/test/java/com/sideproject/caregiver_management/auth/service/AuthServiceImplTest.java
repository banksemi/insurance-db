package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.auth.exception.LoginIdNotMatchException;
import com.sideproject.caregiver_management.auth.exception.NeedAuthenticationException;
import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.service.TenantService;
import com.sideproject.caregiver_management.user.service.TenantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private AuthTokenService authTokenService;

    Long tenantId;
    @BeforeEach
    void setUp() {
        tenantId = tenantService.createTenant("test");
    }
    @Test
    void login() {
        Long userId1 = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        Long userId2 = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test2")
                        .password("test12")
                        .name("test2")
                        .build()
        );
        LoginResponse response1 = authService.login("test", "test12");
        assertEquals(userId1, response1.getUserId());

        LoginResponse response2 = authService.login("test2", "test12");
        assertEquals(userId2, response2.getUserId());

        assertThrows(LoginIdNotMatchException.class, () -> authService.login("new_user", "test12"));
        assertThrows(PasswordNotMatchException.class, () -> authService.login("test", "test1"));
        assertThrows(PasswordNotMatchException.class, () -> authService.login("test", ""));
        assertThrows(NullPointerException.class, () -> authService.login("test", null));
    }

    @Test
    void validateAccessToken() {
        // given
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        User user = tenantService.findUserById(userId);
        String token = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(100));

        assertThrows(ForbiddenAccessException.class, () -> authService.validateAccessToken(token, Auth.Role.ROLE_ADMIN));
        assertEquals(Optional.of(user), authService.validateAccessToken(token, Auth.Role.ROLE_USER));
        assertEquals(Optional.of(user), authService.validateAccessToken(token, Auth.Role.ROLE_GUEST));
    }

    @Test
    void validateAccessToken_Guest() {
        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken(null, Auth.Role.ROLE_ADMIN));
        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken("", Auth.Role.ROLE_ADMIN));
        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken("token", Auth.Role.ROLE_ADMIN));

        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken(null, Auth.Role.ROLE_USER));
        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken("", Auth.Role.ROLE_USER));
        assertThrows(NeedAuthenticationException.class, () -> authService.validateAccessToken("token", Auth.Role.ROLE_USER));

        assertEquals(Optional.empty(), authService.validateAccessToken(null, Auth.Role.ROLE_GUEST));
        assertEquals(Optional.empty(), authService.validateAccessToken("", Auth.Role.ROLE_GUEST));
        assertEquals(Optional.empty(), authService.validateAccessToken("token", Auth.Role.ROLE_GUEST));
    }

    @Test
    void refreshAccessToken() {
        // given
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        User user = tenantService.findUserById(userId);
        String refreshToken = authTokenService.generateAndSaveRefreshToken(user, Instant.now().plusSeconds(100));

        // when
        LoginResponse accessToken = authService.refreshAccessToken(refreshToken);

        // then
        assertEquals(userId, accessToken.getUserId());
        assertEquals(userId, authService.validateAccessToken(accessToken.getAccessToken(), Auth.Role.ROLE_USER).get().getId());
    }

    @Test
    void refreshAccessToken_Invalid() {
        // given
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        User user = tenantService.findUserById(userId);

        // when
        String accessToken = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(100));

        // then
        assertThrows(NeedAuthenticationException.class, ()->authService.refreshAccessToken(accessToken));
        assertThrows(NeedAuthenticationException.class, ()->authService.refreshAccessToken("token"));
        assertThrows(NeedAuthenticationException.class, ()->authService.refreshAccessToken(""));
        assertThrows(NeedAuthenticationException.class, ()->authService.refreshAccessToken(null));
    }
}