package com.sideproject.caregiver_management.auth;

import com.sideproject.caregiver_management.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // 프록시 객체를 사용하여 필요할 때 주입되도록 설정
@Getter @Setter
public class LoginSession {
    private Long userId;
}
