package com.sideproject.caregiver_management.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TenantServiceImplTest {
    @Autowired
    private TenantServiceImpl tenantService;

    @Test
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
}