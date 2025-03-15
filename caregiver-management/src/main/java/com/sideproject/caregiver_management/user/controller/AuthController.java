package com.sideproject.caregiver_management.user.controller;

import com.sideproject.caregiver_management.user.dto.UserLoginRequest;
import com.sideproject.caregiver_management.user.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TenantService tenantService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserLoginRequest request){
        tenantService.login(request.getLoginId(), request.getPassword());
        return "login";
    }
}
