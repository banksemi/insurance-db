package com.sideproject.caregiver_management.auth.controller;

import com.sideproject.caregiver_management.auth.service.AuthService;
import com.sideproject.caregiver_management.user.dto.UserLoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authTokenService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserLoginRequest request){
        authTokenService.login(request.getLoginId(), request.getPassword());
        return "login";
    }
}
