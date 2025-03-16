package com.sideproject.caregiver_management.auth;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final LoginSession loginSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 접근 권한의 기본 값은 Admin
        Auth.Role viewRole = Auth.Role.ROLE_ADMIN;

        // 기본적으로 함수에 정의된 권한을 체크
        Auth authAnnotation = handlerMethod.getMethodAnnotation(Auth.class);

        // 함수에 권한이 설정되어있지 않으면 클래스에 적용된 권한이 있는지 확인
        if (authAnnotation == null) {
            log.debug("authAnnotation is null, check class annotation");

            // .getBean().getClass() 대신 .getBeanType() 으로 주석을 읽자
            // getBean()은 프록시 객체가 반환되어 주석이 없을 수도 있다. (이번 케이스는 둘다 있음)
            // https://stackoverflow.com/questions/75410601/org-springframework-web-method-handlermethod-getbean-getclass-vs-getbeantyp
            Class<?> controllerClass = handlerMethod.getBeanType();
            log.debug("class: {}", controllerClass);
            authAnnotation = controllerClass.getAnnotation(Auth.class);
            log.debug("class authAnnotation: {}", authAnnotation);
        }

        if (authAnnotation != null)
            viewRole = authAnnotation.value();

        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        log.debug("accessToken: {}", accessToken);

        loginSession.setUser(authService.validateAccessToken(accessToken, viewRole));
        return true;
    }
}
