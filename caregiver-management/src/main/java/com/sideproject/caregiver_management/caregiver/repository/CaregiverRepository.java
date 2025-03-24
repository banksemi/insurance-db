package com.sideproject.caregiver_management.caregiver.repository;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CaregiverRepository {
    private final EntityManager em;

    public Caregiver findById(Long id) {
        return em.find(Caregiver.class, id);
    }

    public void save(Caregiver caregiver) {
        em.persist(caregiver);
    }

    public boolean isDuplicatedCaregiver(Long insuranceId, String name, LocalDate startDate) {
        try {
            em.createQuery("select i from Caregiver i " +
                            "where i.insurance.id = :insuranceId " +
                            "and i.name = :name and (i.endDate is null or i.endDate > :startDate)", Caregiver.class)
                    .setParameter("insuranceId", insuranceId)
                    .setParameter("name", name)
                    .setParameter("startDate", startDate)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            System.out.println("CaregiverRepository::isDuplicatedCaregiver");
            return false;
        }
    }
}
