package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.DuplicateResourceException;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.repository.TenantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TenantServiceImplTest {
    @Autowired
    private TenantService tenantService;

    @Test
    @DisplayName("테넌트가 정상적으로 생성되는지 확인")
    void createTenant_ValidName_Success() {
        Long tenantId = tenantService.createTenant("테스트 테넌트");
        assertNotNull(tenantId);

        Tenant tenant = tenantService.findTenantById(tenantId);
        assertEquals(tenant.getId(), tenantId);
        assertEquals(tenant.getName(), "테스트 테넌트");
    }

    @Test
    @DisplayName("테넌트 이름이 지정되지 않는 경우 테넌트 생성 불가")
    void createTenant_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(""));
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(" "));
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(null));
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() {
        Long tenantId = tenantService.createTenant("테스트 테넌트");
        assertNotNull(tenantId);
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("testName")
                        .build()
        );
        assertNotNull(userId);

        User user = tenantService.findUserById(userId);
        assertEquals(userId, user.getId());
        assertEquals("test", user.getLoginId());
        assertNotEquals("test12", user.getPassword());
        assertEquals("testName", user.getName());
        assertThrows(NotFoundUserException.class, () -> tenantService.findUserById(0L));
    }

    @Test
    @DisplayName("존재하지 않는 테넌트 아이디는 NotFoundTenant 예외가 반환되어야함")
    void findTenantById_NotFound() {
        assertThrows(NotFoundTenantException.class, () -> tenantService.findTenantById(0L));
    }

    @Test
    @DisplayName("존재하지 않는 유저 검색시 NotFoundUser 예외가 반환되어야함")
    void findUserById_NotFound() {
        assertThrows(NotFoundUserException.class, () -> tenantService.findUserById(0L));
    }

    @Test
    @DisplayName("동일한 이름의 유저는 중복 생성 가능")
    void createUser_SameName_Success() {
        Long tenantId = tenantService.createTenant("테스트 테넌트");
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("testName")
                        .build()
        );

        Long userId2 = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test2")
                        .password("test12")
                        .name("testName")
                        .build()
        );

        assertNotNull(userId);
        assertNotNull(userId2);
        assertNotEquals(userId, userId2);
    }

    @Test
    @DisplayName("동일한 login ID는 허용되지 않음")
    void createUser_SameLoginId_Failed() {
        Long tenantId = tenantService.createTenant("테스트 테넌트");
        Long userId = tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("testName")
                        .build()
        );

        assertNotNull(userId);
        assertThrows(DuplicateResourceException.class, () -> tenantService.createUser(
                tenantId,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("testName2")
                        .build()
        ));
    }
}