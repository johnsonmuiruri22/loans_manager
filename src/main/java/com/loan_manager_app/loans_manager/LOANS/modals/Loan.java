package com.loan_manager_app.loans_manager.LOANS.modals;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter @Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private BigDecimal loanAmountBorrowed;

    private BigDecimal loanAmountIssued;
    private int loanDuration; // in months
    private BigDecimal interestRate;
    private BigDecimal totalAmountToRepay;
    private BigDecimal customerMonthlyIncome;
    private String loanPurpose;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus; //APPROVED,PENDING,REJECTED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime processedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
