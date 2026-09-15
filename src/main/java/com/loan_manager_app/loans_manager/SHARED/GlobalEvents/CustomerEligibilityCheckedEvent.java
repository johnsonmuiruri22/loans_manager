package com.loan_manager_app.loans_manager.SHARED.GlobalEvents;

public record CustomerEligibilityCheckedEvent(
        Long loanId,
        boolean eligible
) {
}
