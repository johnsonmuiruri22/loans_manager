package com.loan_manager_app.loans_manager.LOANS.DTOS;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanProcessingInfo {
    private Long loanId;
    private BigDecimal loanAmountToIssue;
    private int durationInMonths;
    private BigDecimal interestRate;
    private BigDecimal totalAmountToRepay;
    private String loanPurpose;
    private LocalDateTime processedAt;
}
