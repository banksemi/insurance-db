package com.sideproject.caregiver_management.password.service;

import com.sideproject.caregiver_management.password.dto.EncodedPassword;
import com.sideproject.caregiver_management.password.service.algorithm.PasswordAlgorithm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordAlgorithm mainAlgorithm;
    private final Map<String, PasswordAlgorithm> passwordAlgorithmMap;

    public PasswordServiceImpl(PasswordAlgorithm mainAlgorithm, List<PasswordAlgorithm> passwordAlgorithmList) {
        this.mainAlgorithm = mainAlgorithm;
        this.passwordAlgorithmMap = new HashMap<>();
        for (PasswordAlgorithm passwordAlgorithm : passwordAlgorithmList) {
            passwordAlgorithmMap.put(passwordAlgorithm.getPrefix(), passwordAlgorithm);
        }
    }
    @Override
    public EncodedPassword encode(String rawPassword) {
        return new EncodedPassword(mainAlgorithm.getPrefix(), mainAlgorithm.encode(rawPassword));
    }

    @Override
    public boolean matches(String inputPassword, EncodedPassword encodedPassword) {
        String prefix = encodedPassword.getPrefix();
        String hashedPassword = encodedPassword.getValue();

        if (!passwordAlgorithmMap.containsKey(prefix)) {
            throw new IllegalArgumentException("Unsupported password algorithm");
        }

        return passwordAlgorithmMap.get(prefix).encode(inputPassword).equals(hashedPassword);
    }
}
