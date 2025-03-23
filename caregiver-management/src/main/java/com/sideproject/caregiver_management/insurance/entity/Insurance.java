package com.sideproject.caregiver_management.insurance.entity;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insurance {
    public Insurance(User user){ this.user = user; }

    @Id
    @GeneratedValue
    @Column(name="insurance_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
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

    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
