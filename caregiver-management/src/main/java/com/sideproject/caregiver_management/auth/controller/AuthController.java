package com.sideproject.caregiver_management.auth.controller;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.dto.RefreshTokenRequest;
import com.sideproject.caregiver_management.auth.dto.UserLoginRequest;
import com.sideproject.caregiver_management.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Auth(Auth.Role.ROLE_GUEST)
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid UserLoginRequest request){
        return authService.login(request.getLoginId(), request.getPassword());
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody @Valid RefreshTokenRequest request){
        return authService.refreshAccessToken(request.getRefreshToken());
    }
}
