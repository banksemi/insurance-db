package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.dto.LoginResponse;
import com.sideproject.caregiver_management.auth.exception.LoginIdNotMatchException;
import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
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
}
