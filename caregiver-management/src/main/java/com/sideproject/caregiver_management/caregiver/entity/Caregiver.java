package com.sideproject.caregiver_management.caregiver.entity;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverDateUpdate;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverEndDateAfterContractEndException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverEndDateBeforeStartException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateAfterContractEndException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateBeforeContractStartException;
import com.sideproject.caregiver_management.caregiver.service.amount.CaregiverInsuranceAmountCalculator;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Caregiver {
    public Caregiver(Insurance insurance, Boolean isShared) {
        this.insurance = insurance;
        this.isShared = isShared;
    }

    @Id
    @GeneratedValue
    @Column(name="caregiver_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private LocalDate birthday;

    @Setter
    @Column(nullable = false)
    private Integer genderCode;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Column(nullable = false)
    private Long insuranceAmount;

    @Column(nullable = true)
    private Long refundAmount;

    @Column(nullable = false)
    private Boolean isShared; // 공동 간병 여부

    @Setter
    @Column(nullable = false)
    private Boolean isApproved; // 관리자 승인 여부

    @Setter
    private String memo; // 간병인에 대한 사용자 메모

    public void setDate(CaregiverDateUpdate request) {
        LocalDate wantedStartDate = request.isUpdateStartDate() ? request.getStartDate() : this.startDate;
        LocalDate wantedEndDate = request.isUpdateEndDate() ? request.getEndDate() : this.endDate;

        // 유효성 검사
        if (wantedEndDate != null) {
            if (wantedEndDate.isBefore(wantedStartDate))
                throw new CaregiverEndDateBeforeStartException();

            if (wantedEndDate.isAfter(insurance.getEndDate()))
                throw new CaregiverEndDateAfterContractEndException();
        }

        if (wantedStartDate.isBefore(insurance.getStartDate()))
            throw new CaregiverStartDateBeforeContractStartException();

        if (!wantedStartDate.isBefore(insurance.getEndDate())) // 보험 만료일에 정확히 일치하는 것도 예외 반환
            throw new CaregiverStartDateAfterContractEndException();

        // 요청 내역 반영
        this.startDate = wantedStartDate;
        this.endDate = wantedEndDate;
    }

    public void calculateAmounts(CaregiverInsuranceAmountCalculator calculator) {
        // Todo: 사실 엔티티가 직접 계산을 하는것, Calculator를 받아 계산을 하는것, 외부에서 직접 Setter를 통해 조작하는 것에 대해 어떤게 가장 좋을지는 여전히 고민이 된다.
        //  (https://www.inflearn.com/community/questions/24903/%EC%97%94%ED%8B%B0%ED%8B%B0%EC%97%90-%EC%9D%98%EC%A1%B4%EC%84%B1-%EC%A3%BC%EC%9E%85%EC%9D%B4-%ED%95%84%EC%9A%94%ED%95%9C-%EA%B2%BD%EC%9A%B0)
        insuranceAmount = calculator.getInsuranceAmount(this);
        refundAmount = calculator.getRefundAmount(this);
    }
}
