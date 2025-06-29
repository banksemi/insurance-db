package com.sideproject.caregiver_management.user.controller;

import com.sideproject.caregiver_management.user.dto.TenantPublicInformation;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @GetMapping("/{tenant_id}")
    public TenantPublicInformation getTenant(@PathVariable("tenant_id") Long tenantId){
        // 모두가 조회할 수 있는 public 정보만 반환
        Tenant tenant = tenantService.findTenantById(tenantId);
        return TenantPublicInformation.builder().name(tenant.getName()).build();
    }
}
