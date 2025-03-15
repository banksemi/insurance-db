package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundTenantException;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.repository.TenantRepository;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TenantServiceImplTest {
    @Autowired
    private TenantServiceImpl tenantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Test
    // @Transactional // 테스트 케이스 안에서는 롤백 기능을 포함한다.
    void createTenant_ValidName_Success() {
        Long tenant_id = tenantService.createTenant("테스트 테넌트");
        assertNotNull(tenant_id);
    }

    @Test
    void createTenant_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(""));
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(" "));
        assertThrows(IllegalArgumentException.class, () -> tenantService.createTenant(null));
    }

    @Test
    void createUser() {
        Long tenant_id = tenantService.createTenant("테스트 테넌트");
        assertNotNull(tenant_id);
        Long user_id = tenantService.createUser(
                tenant_id,
                UserCreateRequest.builder()
                        .loginId("test")
                        .password("test12")
                        .name("test")
                        .build()
        );
        assertNotNull(user_id);

        User user = userRepository.findOne(user_id);
        assertNotEquals(user.getPassword(), "test12");
    }

    @Test
    void notFoundUser() {
        assertThrows(NotFoundUserException.class, () -> tenantService.login(null, null));
        assertThrows(NotFoundUserException.class, () -> tenantService.login("", null));
        assertThrows(NotFoundUserException.class, () -> tenantService.login("Not found", null));
    }

    @Test
    @Transactional
    void login() {
        Tenant tenant = new Tenant();
        tenant.setName("name");
        tenantRepository.save(tenant);

        Long user1_id = tenantService.createUser(
                tenant.getId(),
                UserCreateRequest
                        .builder()
                        .loginId("test")
                        .password("test12")
                        .name("test1")
                        .build()
        );
        Long user2_id = tenantService.createUser(
                tenant.getId(),
                UserCreateRequest
                        .builder()
                        .loginId("test2")
                        .password("test12")
                        .name("test2")
                        .build()
        );
        User login_user = tenantService.login("test", "test12");
        assertEquals(user1_id, login_user.getId());

        User login_user2 = tenantService.login("test2", "test12");
        assertEquals(user2_id, login_user2.getId());

        assertThrows(PasswordNotMatchException.class, () -> tenantService.login("test", "test1"));
        assertThrows(PasswordNotMatchException.class, () -> tenantService.login("test", ""));
        assertThrows(NullPointerException.class, () -> tenantService.login("test", null));
    }

    @Test
    void findTenantById() {
        Tenant tenant = new Tenant();
        tenant.setName("name");
        tenantRepository.save(tenant);

        Tenant findTenant = tenantService.findTenantById(tenant.getId());

        assertEquals(tenant.getId(), findTenant.getId());
        assertEquals(tenant.getName(), findTenant.getName());
        assertThrows(NotFoundTenantException.class, () -> tenantService.findTenantById(0L));

    }
}