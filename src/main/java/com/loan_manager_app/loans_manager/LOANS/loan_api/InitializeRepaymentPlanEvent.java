package com.loan_manager_app.loans_manager.LOANS.loan_api;

import java.math.BigDecimal;

public record InitializeRepaymentPlanEvent(
        Long loanId,
        Long customerId,
        BigDecimal loanAmountIssuedToCustomer,
        BigDecimal totalAmountToBeRepaid,
        Integer loanDuration
) {
}
