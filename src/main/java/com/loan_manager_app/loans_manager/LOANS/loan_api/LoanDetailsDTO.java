package com.loan_manager_app.loans_manager.LOANS.loan_api;


import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanDetailsDTO {
    private Long loanId;
    private Long customerId;
    private BigDecimal loanAmount;
    private BigDecimal loanAmountIssued;
    private int loanDuration; // in months
    private BigDecimal interestRate;
    private BigDecimal totalAmount;
    private String loanPurpose;
    private String loanStatus;
    private String issuedAt;
}
