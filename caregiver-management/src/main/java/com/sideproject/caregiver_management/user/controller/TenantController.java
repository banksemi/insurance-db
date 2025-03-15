package com.sideproject.caregiver_management.user.controller;

import com.sideproject.caregiver_management.user.dto.TenantPublicInformation;
import com.sideproject.caregiver_management.user.dto.UserLoginRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @GetMapping("/{tenant_id}")
    public TenantPublicInformation getTenant(@PathVariable("tenant_id") Long tenantId){
        Tenant tenant = tenantService.findTenantById(tenantId);
        return TenantPublicInformation.builder().name(tenant.getName()).build();
    }
}
