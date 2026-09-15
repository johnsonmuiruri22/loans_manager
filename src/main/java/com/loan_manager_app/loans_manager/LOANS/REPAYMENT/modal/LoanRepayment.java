package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.modal;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long loanId;
    private Long customerId;
    private BigDecimal amountIssuedToCustomer;
    private BigDecimal amountPaid;
    private BigDecimal totalAmountToBeRepaid;
    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus repaymentStatus;

    @CreationTimestamp
    private final LocalDateTime repaidAt = LocalDateTime.now();
}
