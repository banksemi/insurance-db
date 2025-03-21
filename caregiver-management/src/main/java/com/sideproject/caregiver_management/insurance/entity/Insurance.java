package com.sideproject.caregiver_management.insurance.entity;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
public class Insurance {
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "personal_insurance_fee", nullable = false)
    private Long personalInsuranceFee;

    @Column(name = "shared_insurance_fee", nullable = false)
    private Long sharedInsuranceFee;

    public void updateInsurance(InsuranceUpdateRequest updateRequest) {
        if (updateRequest.getStartDate() != null)
            setStartDate(updateRequest.getStartDate());

        if (updateRequest.getEndDate() != null)
            setEndDate(updateRequest.getEndDate());

        if (updateRequest.getPolicyNumber() != null)
            setPolicyNumber(updateRequest.getPolicyNumber());

        if (updateRequest.getPersonalInsuranceFee() != null)
            setPersonalInsuranceFee(updateRequest.getPersonalInsuranceFee());

        if (updateRequest.getSharedInsuranceFee() != null)
            setSharedInsuranceFee(updateRequest.getSharedInsuranceFee());
    }
}
