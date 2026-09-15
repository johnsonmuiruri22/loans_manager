package com.loan_manager_app.loans_manager.LOANS.loan_api;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanSummary(
        Long customerId,
        BigDecimal loanAmountRequested,
        BigDecimal loanAmountIssued,
        int loanDuration,
        BigDecimal totalAmountToBeRepaid
) {
}
