package com.sideproject.caregiver_management;

import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.exception.DuplicateResourceException;
import com.sideproject.caregiver_management.user.service.TenantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InitService {
    private final TenantService tenantService;
    @PostConstruct
    public void init(){
        System.out.println("InitService is called");
        try {
            Long tenant_id = tenantService.createTenant("DEMO Tenant");
            tenantService.createUser(tenant_id, UserCreateRequest
                    .builder()
                    .loginId("admin")
                    .password("admin12")
                    .name("admin")
                    .build()
            );
        } catch (DuplicateResourceException e) {

        }
    }
}
