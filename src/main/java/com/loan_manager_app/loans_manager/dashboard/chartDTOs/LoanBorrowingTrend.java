package com.loan_manager_app.loans_manager.dashboard.chartDTOs;


import lombok.*;

import java.math.BigDecimal;


@Builder
public record LoanBorrowingTrend(
        String period,
        BigDecimal amount
) {
}
