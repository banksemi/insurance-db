package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.entity.AuthToken;
import com.sideproject.caregiver_management.auth.exception.NotFoundTokenException;
import com.sideproject.caregiver_management.auth.repository.TokenRepository;
import com.sideproject.caregiver_management.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class DBAuthTokenService implements AuthTokenService {
    private final TokenRepository tokenRepository;
    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[256];
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Override
    @Transactional
    public String generateAndSaveAccessToken(User user, Instant expireAt) {
        AuthToken authToken = AuthToken.builder().key(generateToken()).type("access").user(user).expiredAt(
                expireAt
        ).build();
        tokenRepository.save(authToken);
        return authToken.getKey();
    }

    @Override
    @Transactional
    public String generateAndSaveRefreshToken(User user, Instant expireAt) {
        AuthToken authToken = AuthToken.builder().key(generateToken()).type("refresh").user(user).expiredAt(
                expireAt
        ).build();
        tokenRepository.save(authToken);
        return authToken.getKey();
    }

    @Override
    public User getUserFromAccessToken(String accessToken) throws NotFoundTokenException {
        if (accessToken == null)
            throw new NotFoundTokenException();

        AuthToken authToken = tokenRepository.findOne(accessToken);
        if (authToken == null)
            throw new NotFoundTokenException();

        if (authToken.getExpiredAt().isBefore(Instant.now())) {
            throw new NotFoundTokenException();
        }
        if (authToken.getType().equals("access")) {
            return authToken.getUser();
        } else {
            throw new NotFoundTokenException();
        }
    }

    @Override
    public User getUserFromRefreshToken(String accessToken) throws NotFoundTokenException {
        if (accessToken == null)
            throw new NotFoundTokenException();

        AuthToken authToken = tokenRepository.findOne(accessToken);
        if (authToken == null)
            throw new NotFoundTokenException();

        if (authToken.getExpiredAt().isBefore(Instant.now())) {
            throw new NotFoundTokenException();
        }

        if (authToken.getType().equals("refresh")) {
            return authToken.getUser();
        } else {
            throw new NotFoundTokenException();
        }
    }

    @Override
    public void removeAccessToken(String accessToken) {

    }

    @Override
    public void removeRefreshToken(String refreshToken) {

    }
}
