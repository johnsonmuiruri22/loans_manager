package com.loan_manager_app.loans_manager.dashboard.utils;


import com.loan_manager_app.loans_manager.dashboard.dto.DashboardDateRange;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DashboardPeriodCalculator {

    public DashboardDateRange calculate(
            DashboardPeriod period
    ){
        LocalDate today = LocalDate.now();

        return switch (period){
            case LAST_7_DAYS -> {
                LocalDate start =
                        today.minusDays(6);

                LocalDate end =
                        today.plusDays(1);

                yield new DashboardDateRange(
                        start.atStartOfDay(),
                        end.atStartOfDay()
                );
            }

            case LAST_30_DAYS -> {

                LocalDate start =
                        today.minusDays(29);

                LocalDate end =
                        today.plusDays(1);

                yield new DashboardDateRange(
                        start.atStartOfDay(),
                        end.atStartOfDay()
                );
            }

            case LAST_6_MONTHS -> {

                LocalDate start =
                        today.minusMonths(5)
                                .withDayOfMonth(1);

                LocalDate end =
                        today.plusMonths(1)
                                .withDayOfMonth(1);

                yield new DashboardDateRange(
                        start.atStartOfDay(),
                        end.atStartOfDay()
                );
            }

            case LAST_12_MONTHS -> {

                LocalDate start =
                        today.minusMonths(11)
                                .withDayOfMonth(1);

                LocalDate end =
                        today.plusMonths(1)
                                .withDayOfMonth(1);

                yield new DashboardDateRange(
                        start.atStartOfDay(),
                        end.atStartOfDay()
                );
            }
        };

    }
}
