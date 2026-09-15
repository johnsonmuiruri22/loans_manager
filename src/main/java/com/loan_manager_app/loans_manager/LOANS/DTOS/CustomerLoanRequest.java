package com.loan_manager_app.loans_manager.LOANS.DTOS;


import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CustomerLoanRequest(
        String message,
        Long customerId,
        String customerFullName,
        String customerCreditStatus,
        String customerEmail,
        BigDecimal loanAmount,
        int durationInMonths,
        String loanPurpose,
        LocalDateTime requestedAt
) {
}
