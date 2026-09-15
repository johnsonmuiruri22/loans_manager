package com.loan_manager_app.loans_manager.SHARED.GlobalEvents;

import java.math.BigDecimal;

public record LoanRequestedEvent(
        Long loanId,
        Long customerId
) {
}
