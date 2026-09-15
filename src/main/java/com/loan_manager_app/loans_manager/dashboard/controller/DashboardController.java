package com.loan_manager_app.loans_manager.dashboard.controller;


import com.loan_manager_app.loans_manager.dashboard.chartDTOs.CustomerRegistrationTrend;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.LoanBorrowingTrend;
import com.loan_manager_app.loans_manager.dashboard.dto.DashboardSummaryDTO;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;
import com.loan_manager_app.loans_manager.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loan-management/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {

        return ResponseEntity.ok(
                dashboardService.getSummary()
        );
    }

    @GetMapping("/loan-trends")
    public ResponseEntity<List<LoanBorrowingTrend>> getLoanBorrowingTrend(
            @RequestParam(defaultValue = "LAST_12_MONTHS")
            DashboardPeriod period) {

        return ResponseEntity.ok(
                dashboardService.getLoanBorrowingTrend(period)
        );
    }

    @GetMapping("/customer-trends")
    public ResponseEntity<List<CustomerRegistrationTrend>>
    getCustomerRegistrationTrend(
            @RequestParam(defaultValue = "LAST_12_MONTHS")
            DashboardPeriod period) {

        return ResponseEntity.ok(
                dashboardService.getCustomerRegistrationTrend(period)
        );
    }
}
