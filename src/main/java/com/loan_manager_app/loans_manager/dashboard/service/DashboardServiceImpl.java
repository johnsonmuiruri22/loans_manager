package com.loan_manager_app.loans_manager.dashboard.service;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerDashboardQuery;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanDashboardQuery;
import com.loan_manager_app.loans_manager.LOANS.loan_api.RepaymentDashboardQuery;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.CustomerRegistrationTrend;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.LoanBorrowingTrend;
import com.loan_manager_app.loans_manager.dashboard.dto.DashboardSummaryDTO;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {
    private final CustomerDashboardQuery customerDashboardQuery;
    private final LoanDashboardQuery loanDashboardQuery;
    private final RepaymentDashboardQuery repaymentDashboardQuery;

    @Override
    public DashboardSummaryDTO getSummary() {

        LocalDateTime startOfMonth =
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay();

        return DashboardSummaryDTO.builder()
                .totalCustomers(
                        customerDashboardQuery.countCustomers()
                )
                .customersThisMonth(
                        customerDashboardQuery
                                .countCustomersRegisteredSince(
                                        startOfMonth
                                )
                )
                .totalLoans(
                        loanDashboardQuery.countLoans()
                )
                .activeLoans(
                        loanDashboardQuery.countActiveLoans()
                )
                .totalAmountBorrowed(
                        loanDashboardQuery.getTotalAmountBorrowed()
                )
                .totalAmountIssued(
                        loanDashboardQuery.getTotalAmountIssued()
                )
                .totalRepayments(
                        repaymentDashboardQuery.getTotalAmountPaid()
                )
                .outstandingBalance(
                        repaymentDashboardQuery.getOutstandingBalance()
                )
                .build();
    }


    @Override
    public List<LoanBorrowingTrend> getLoanBorrowingTrend(
            DashboardPeriod period
    ) {

        return loanDashboardQuery
                .getBorrowingTrend(period)
                .stream()
                .map(trend ->
                        new LoanBorrowingTrend(
                                trend.period(),
                                trend.amount()
                        )
                )
                .toList();
    }

    @Override
    public List<CustomerRegistrationTrend> getCustomerRegistrationTrend(
            DashboardPeriod period
    ) {

        return customerDashboardQuery
                .getRegistrationTrend(period)
                .stream()
                .map(trend ->
                        new CustomerRegistrationTrend(
                                trend.period(),
                                trend.customers()
                        )
                )
                .toList();
    }
}
