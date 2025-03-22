package com.sideproject.caregiver_management.auth;

import com.sideproject.caregiver_management.auth.dto.LoginInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // 프록시 객체를 사용하여 필요할 때 주입되도록 설정
@Getter
@Setter(AccessLevel.PRIVATE)
public class LoginSession {
    private Long tenantId;
    private Long userId;

    public void setLoginInfo(LoginInfo info) {
        setTenantId(info.getTenantId());
        setUserId(info.getUserId());
    }
}
