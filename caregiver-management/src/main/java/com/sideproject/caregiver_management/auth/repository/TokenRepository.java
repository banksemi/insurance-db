package com.sideproject.caregiver_management.auth.repository;

import com.sideproject.caregiver_management.auth.entity.AuthToken;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {
    private final EntityManager em;

    public AuthToken findOne(String key){
        return em.find(AuthToken.class, key);
    }

    public void save(AuthToken authToken){
        em.persist(authToken);
    }
}
