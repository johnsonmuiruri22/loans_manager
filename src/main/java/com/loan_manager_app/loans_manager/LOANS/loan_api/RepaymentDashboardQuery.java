package com.loan_manager_app.loans_manager.LOANS.loan_api;

import java.math.BigDecimal;

public interface RepaymentDashboardQuery {
    BigDecimal getTotalAmountPaid();
    BigDecimal getOutstandingBalance();
}
