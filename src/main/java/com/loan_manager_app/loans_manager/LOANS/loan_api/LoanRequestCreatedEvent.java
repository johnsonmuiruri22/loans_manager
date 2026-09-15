package com.loan_manager_app.loans_manager.LOANS.loan_api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanRequestCreatedEvent(
        Long customerId,
        String customerName,
        BigDecimal amountRequested,
        String loanPurpose,
        String customerCreditStatus,
        BigDecimal customerIncomePerMonth,
        int durationToRepay,
        LocalDateTime requestedAt
) {
}
