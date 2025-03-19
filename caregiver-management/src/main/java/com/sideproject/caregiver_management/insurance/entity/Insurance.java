package com.sideproject.caregiver_management.insurance.entity;

import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class Insurance {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "personal_insurance_fee")
    private Long personalInsuranceFee;

    @Column(name = "shared_insurance_fee")
    private Long sharedInsuranceFee;
}
