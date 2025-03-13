package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void notFoundUser() {
        assertThrows(NotFoundUserException.class, () -> authService.login(null, null));
        assertThrows(NotFoundUserException.class, () -> authService.login("", null));
        assertThrows(NotFoundUserException.class, () -> authService.login("Not found", null));
    }
}