package com.loan_manager_app.loans_manager.dashboard.service;

import com.loan_manager_app.loans_manager.dashboard.chartDTOs.CustomerRegistrationTrend;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.LoanBorrowingTrend;
import com.loan_manager_app.loans_manager.dashboard.dto.DashboardSummaryDTO;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;

import java.util.List;

public interface DashboardService {
    DashboardSummaryDTO getSummary();
    List<LoanBorrowingTrend> getLoanBorrowingTrend(DashboardPeriod dashboardPeriod);
    List<CustomerRegistrationTrend> getCustomerRegistrationTrend(DashboardPeriod period);
}
