package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.exception.PasswordNotMatchException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.auth.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public User login(String loginId, String password) throws NotFoundUserException, PasswordNotMatchException {
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            throw new NotFoundUserException("사용자를 찾을 수 없습니다.");
        }

        if (!passwordService.matches(password, user.get().getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        return user.get();
    }
}
