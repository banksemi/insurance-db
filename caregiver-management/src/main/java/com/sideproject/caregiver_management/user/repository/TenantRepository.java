package com.sideproject.caregiver_management.user.repository;

import com.sideproject.caregiver_management.user.entity.Tenant;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TenantRepository {
    private final EntityManager em;

    public void save(Tenant tenant){
        em.persist(tenant);
        em.flush();
    }

    public Tenant findOne(Long id){
        return em.find(Tenant.class, id);
    }
}
