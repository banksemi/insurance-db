package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.*;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final AuthTokenService authTokenService;

    @Override
    public LoginResponse login(String loginId, String password) throws LoginIdNotMatchException, PasswordNotMatchException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            throw new LoginIdNotMatchException("사용자를 찾을 수 없습니다.");
        }

        if (!passwordService.matches(password, user.get().getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        Instant expireAt = Instant.now().plusSeconds(3600);
        String accessToken = authTokenService.generateAndSaveAccessToken(user.get(), expireAt);
        String refreshToken = authTokenService.generateAndSaveRefreshToken(user.get(), Instant.now().plus(30, ChronoUnit.DAYS));

        return LoginResponse
                .builder()
                .userId(user.get().getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireAt(expireAt.getEpochSecond())
                .build();
    }

    @Override
    public Optional<User> validateAccessToken(String accessToken, Auth.Role role) throws NeedAuthenticationException, ForbiddenAccessException {
        Optional<User> loginUser;
        try {
            loginUser = Optional.of(authTokenService.getUserFromAccessToken(accessToken));
        } catch (NotFoundTokenException ex) {
            loginUser = Optional.empty();
        }

        if (loginUser.isEmpty() && role != Auth.Role.ROLE_GUEST)
            throw new NeedAuthenticationException();

        // Todo: 어드민 권한 구현 필요, 현재는 무조건 오류로 반환
        if (role == Auth.Role.ROLE_ADMIN)
            throw new ForbiddenAccessException();

        return loginUser;
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) throws NeedAuthenticationException {
        try {
            User user = authTokenService.getUserFromRefreshToken(refreshToken);
            Instant expireAt = Instant.now().plusSeconds(3600);
            String accessToken = authTokenService.generateAndSaveAccessToken(user, expireAt);

            return LoginResponse.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expireAt(expireAt.getEpochSecond())
                    .build();
        } catch (NotFoundTokenException e) {
            throw new NeedAuthenticationException("토큰이 유효하지 않습니다.");
        }
    }
}
