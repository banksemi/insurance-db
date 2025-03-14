package com.sideproject.caregiver_management.user.service;

import com.sideproject.caregiver_management.user.service.password.PasswordAlgorithm;
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
        this.passwordAlgorithmMap = new HashMap<String, PasswordAlgorithm>();
        for (PasswordAlgorithm passwordAlgorithm : passwordAlgorithmList) {
            passwordAlgorithmMap.put(passwordAlgorithm.getPrefix(), passwordAlgorithm);
        }
    }
    @Override
    public String encode(String rawPassword) {
        return mainAlgorithm.getPrefix() + ":" + mainAlgorithm.encode(rawPassword);
    }

    @Override
    public boolean matches(String inputPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid password format");
        }
        String prefix = parts[0];
        String hashedPassword = parts[1];
        if (!passwordAlgorithmMap.containsKey(prefix)) {
            throw new IllegalArgumentException("Unsupported password algorithm");
        }
        return passwordAlgorithmMap.get(prefix).encode(inputPassword).equals(hashedPassword);
    }
}
