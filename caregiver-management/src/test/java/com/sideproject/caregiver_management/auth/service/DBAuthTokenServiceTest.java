package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.NotFoundTokenException;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DBAuthTokenServiceTest {
    private static final int MIN_TOKEN_LENGTH = 256;

    @Autowired
    private DBAuthTokenService authTokenService;
    @Autowired
    private TenantService tenantService;

    private User user;

    @BeforeEach
    void createUser() {
        Long tenantId = tenantService.createTenant("test");
        Tenant tenant = tenantService.findTenantById(tenantId);

        Long userId = tenantService.createUser(tenantId, UserCreateRequest.builder().loginId("login1")
                .name("name").password("password").build());

        user = tenantService.findUserById(userId);
    }
    @Test
    void generateAndSaveAccessToken() {
        String token = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(10000L));

        assertNotNull(token);
        assertTrue(token.length() >= MIN_TOKEN_LENGTH);
    }

    @Test
    void generateAndSaveRefreshToken() {
        String token = authTokenService.generateAndSaveRefreshToken(user, Instant.now().plusSeconds(10000L));

        assertNotNull(token);
        assertTrue(token.length() >= MIN_TOKEN_LENGTH);
    }

    @Test
    void getUserFromAccessToken() {
        String token = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(10000L));

        try {
            User user = authTokenService.getUserFromAccessToken(token);
        } catch (NotFoundTokenException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(user);
        assertEquals(this.user.getId(), user.getId());
    }

    @Test
    void getUserFromAccessToken_ExpiredToken() {
        String token = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(-1L));

        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromAccessToken(token));
    }

    @Test
    void InvalidToken() {
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromAccessToken("token"));
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromAccessToken(""));
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromAccessToken(null));

        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromRefreshToken("token"));
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromRefreshToken(""));
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromRefreshToken(null));
    }

    @Test
    void getUserFromRefreshToken() {
        String token = authTokenService.generateAndSaveRefreshToken(user, Instant.now().plusSeconds(10000L));
        try {
            User user = authTokenService.getUserFromRefreshToken(token);
        } catch (NotFoundTokenException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(user);
        assertEquals(this.user.getId(), user.getId());
    }

    @Test
    void getUserFromRefreshToken_ExpiredToken() {
        String token = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(-1L));

        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromRefreshToken(token));
    }


    @Test
    void invalidTokenType() {
        String accessToken = authTokenService.generateAndSaveAccessToken(user, Instant.now().plusSeconds(10000L));
        String refreshToken = authTokenService.generateAndSaveRefreshToken(user, Instant.now().plusSeconds(10000L));

        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromRefreshToken(accessToken));
        assertThrows(NotFoundTokenException.class, () -> authTokenService.getUserFromAccessToken(refreshToken));
    }
}