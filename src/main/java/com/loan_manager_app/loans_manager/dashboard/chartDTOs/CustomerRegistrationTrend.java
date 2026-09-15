package com.loan_manager_app.loans_manager.dashboard.chartDTOs;


import lombok.*;

@Builder
public record CustomerRegistrationTrend(
        String period,
        long customers
) {
}
