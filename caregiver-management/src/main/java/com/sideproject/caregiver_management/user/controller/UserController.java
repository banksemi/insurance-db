package com.sideproject.caregiver_management.user.controller;

import com.sideproject.caregiver_management.auth.LoginSession;
import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.user.dto.UserResponse;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final TenantService tenantService;
    private final LoginSession loginSession;

    @GetMapping("/me")
    @Auth(Auth.Role.ROLE_USER)
    public UserResponse getUser(){
        User user = tenantService.findUserById(loginSession.getUserId());
        return UserResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .build();
    }
}
