package com.sideproject.caregiver_management.user.controller;

import com.sideproject.caregiver_management.auth.LoginSession;
import com.sideproject.caregiver_management.auth.annotation.AuthorizeUser;
import com.sideproject.caregiver_management.user.dto.UserResponse;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@AuthorizeUser
public class UserController {
    private final TenantService tenantService;

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") Long userId) {
        User user = tenantService.findUserById(userId);
        return UserResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .isAdmin(user.getIsAdmin())
                .build();
    }
}
