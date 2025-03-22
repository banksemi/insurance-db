package com.sideproject.caregiver_management.auth.service;

import com.sideproject.caregiver_management.auth.LoginSession;
import com.sideproject.caregiver_management.auth.exception.ForbiddenAccessException;
import com.sideproject.caregiver_management.user.entity.User;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizationServiceImpl implements AuthorizationService {
    private final LoginSession loginSession;
    private final TenantService tenantService;

    @Override
    public void validateAccessToUser(Long userId) throws ForbiddenAccessException, NotFoundUserException {
        // 본인 리스소에 대한 요청인 경우
        if (Objects.equals(loginSession.getUserId(), userId)) {
            return;
        }

        // 다른 사용자의 리소스 요청인 경우
        User loginUser = tenantService.findUserById(loginSession.getUserId());
        if (!loginUser.getIsAdmin()) {
            throw new ForbiddenAccessException();
        }

        // 테넌트가 다르면 없는 사용자로 취급
        User user = tenantService.findUserById(userId); // throws NotFoundUserException
        if (!Objects.equals(user.getTenant().getId(), loginSession.getTenantId())) {
            throw new NotFoundUserException();
        }
    }
}
