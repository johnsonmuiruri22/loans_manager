package com.loan_manager_app.loans_manager.LOANS.loan_api;

import com.loan_manager_app.loans_manager.dashboard.chartDTOs.LoanBorrowingTrend;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface LoanDashboardQuery {
    long countLoans();
    long countActiveLoans();
    BigDecimal getTotalAmountBorrowed();
    BigDecimal getTotalAmountIssued();
    List<LoanBorrowingTrend> getBorrowingTrend(
            DashboardPeriod period
    );
}
