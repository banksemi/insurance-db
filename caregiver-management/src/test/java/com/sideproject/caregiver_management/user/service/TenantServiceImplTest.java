package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.entity.User;
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
}