package com.sideproject.caregiver_management;

import com.sideproject.caregiver_management.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API를 위해 CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .httpBasic(basic -> basic.disable()) // HTTP Basic 인증 비활성화
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/server/status").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()          // 로그인/인증 API는 모두 허용
                        .requestMatchers("/api/v1/tenants/**").permitAll()       // 테넌트 정보는 허용 (로그인 화면용)
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")    // ADMIN 권한 필요
                        .requestMatchers("/api/v1/users/**").hasRole("USER")     // USER 권한 필요
                        .requestMatchers("/api/**").authenticated()              // 나머지 API는 로그인 필요
                        .anyRequest().permitAll())
                .logout(logout -> logout.disable());

        return http.build();
    }

}
