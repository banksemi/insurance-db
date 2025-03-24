package com.sideproject.caregiver_management.insurance.repository;

import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.exception.NotFoundInsuranceException;
import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InsuranceRepository {
    private final EntityManager em;

    public void save(Insurance insurance){
        em.persist(insurance);
    }

    public Optional<Insurance> findById(Long id) {
        return Optional.ofNullable(em.find(Insurance.class, id));
    }

    public Optional<Insurance> findByUserID(Long userId){
        try {
            return Optional.of(
                    em.createQuery("select i from Insurance i where i.user.id = :userId", Insurance.class)
                            .setParameter("userId", userId)
                            .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
