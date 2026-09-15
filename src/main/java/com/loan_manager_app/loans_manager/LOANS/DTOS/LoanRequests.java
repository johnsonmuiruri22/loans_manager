package com.loan_manager_app.loans_manager.LOANS.DTOS;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequests {
    private Long loanId;
    private CustomerSummary customerInformation;
    private BigDecimal loanAmountRequested;
    private BigDecimal loanAmountIssued;
    private int loanDuration;
    private BigDecimal loanInterestRate;
    private BigDecimal totalAmountToRepay;
    private String loanPurpose;
    private String loanStatus;
}
