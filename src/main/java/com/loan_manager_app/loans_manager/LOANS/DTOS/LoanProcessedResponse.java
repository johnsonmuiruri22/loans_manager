package com.loan_manager_app.loans_manager.LOANS.DTOS;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record LoanProcessedResponse(
        String message,
        BigDecimal amountIssued,
        BigDecimal interestRateIssued,
        BigDecimal totalAmountToRepay,
        int loanTermInMonths
) {
}
