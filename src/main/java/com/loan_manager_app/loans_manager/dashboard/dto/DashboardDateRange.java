package com.loan_manager_app.loans_manager.dashboard.dto;

import java.time.LocalDateTime;

public record DashboardDateRange(
        LocalDateTime start,
        LocalDateTime end
) {
}
