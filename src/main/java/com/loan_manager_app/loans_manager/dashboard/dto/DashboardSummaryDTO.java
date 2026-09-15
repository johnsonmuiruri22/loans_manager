package com.loan_manager_app.loans_manager.dashboard.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {
    private long totalCustomers;
    private long customersThisMonth;
    private long totalLoans;
    private long activeLoans;
    private BigDecimal totalAmountBorrowed;
    private BigDecimal totalAmountIssued;
    private BigDecimal totalRepayments;
    private BigDecimal outstandingBalance;
}
