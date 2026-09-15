package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;

import com.loan_manager_app.loans_manager.dashboard.chartDTOs.CustomerRegistrationTrend;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerDashboardQuery {
    long countCustomers();
    long countCustomersRegisteredSince(LocalDateTime start);
    List<CustomerRegistrationTrend> getRegistrationTrend(
            DashboardPeriod period
    );
}
