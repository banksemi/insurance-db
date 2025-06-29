package com.sideproject.caregiver_management.auth;

import com.sideproject.caregiver_management.auth.dto.LoginInfo;
import com.sideproject.caregiver_management.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final LoginSession loginSession;

    private Optional<String> extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            log.debug("Authorization header is null");
            return Optional.empty();
        }

        if (!bearerToken.startsWith("Bearer ")) {
            log.debug("Authorization header is not Bearer");
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(7));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter executed for: {}", request.getRequestURI());
        Optional<String> accessToken = extractToken(request);
        if (accessToken.isPresent()) {
            log.info("accessToken: {}", accessToken);
            Optional<LoginInfo> loginInfo = authService.validateAccessToken(accessToken.get());
            if (loginInfo.isPresent()) {
                loginSession.setLoginInfo(loginInfo.get());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                if (loginInfo.get().getIsAdmin()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        loginInfo.get(), null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User authenticated: {}, authorities: {}", loginInfo.get().getUserId(), authorities);
            }
        }
        filterChain.doFilter(request, response);

    }
}
