package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @Override
    public boolean login(String loginId, String password) throws NotFoundUserException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            throw new NotFoundUserException("사용자를 찾을 수 없습니다.");
        }
        return false;
    }
}
