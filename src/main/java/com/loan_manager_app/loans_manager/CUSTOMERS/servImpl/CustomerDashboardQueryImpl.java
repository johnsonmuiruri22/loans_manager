package com.loan_manager_app.loans_manager.CUSTOMERS.servImpl;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerDashboardQuery;
import com.loan_manager_app.loans_manager.CUSTOMERS.repo.CustomerRepository;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.CustomerRegistrationTrend;
import com.loan_manager_app.loans_manager.dashboard.dto.DashboardDateRange;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;
import com.loan_manager_app.loans_manager.dashboard.utils.DashboardPeriodCalculator;
import com.loan_manager_app.loans_manager.dashboard.utils.DashboardTimelineGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDashboardQueryImpl implements CustomerDashboardQuery {
    private final CustomerRepository customerRepository;
    private final DashboardPeriodCalculator dashboardPeriodCalculator;
    private final DashboardTimelineGenerator dashboardTimelineGenerator;

    @Override
    public long countCustomers() {
        return customerRepository.count();
    }

    @Override
    public long countCustomersRegisteredSince(LocalDateTime start) {
        return customerRepository.countByCreatedAtAfter(start);
    }

    @Override
    public List<CustomerRegistrationTrend> getRegistrationTrend(DashboardPeriod period) {
        DashboardDateRange range =
                dashboardPeriodCalculator.calculate(period);

        if (period == DashboardPeriod.LAST_7_DAYS ||
                period == DashboardPeriod.LAST_30_DAYS) {

            return getDailyRegistrationTrend(range);
        }
        return getMonthlyRegistrationTrend(range);
    }

    private List<CustomerRegistrationTrend>
    getMonthlyRegistrationTrend(
            DashboardDateRange range) {

        List<Object[]> results =
                customerRepository.getRegistrationTrend(
                        range.start(),
                        range.end()
                );

        Map<String, Long> databaseValues =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        row -> (String) row[0],
                                        row -> ((Number) row[1])
                                                .longValue()
                                )
                        );

        List<YearMonth> months =
                dashboardTimelineGenerator.generateMonths(
                        range.start(),
                        range.end()
                );

        return months.stream()
                .map(month -> {

                    String key =
                            month.toString();

                    long customers =
                            databaseValues.getOrDefault(
                                    key,
                                    0L
                            );

                    return new CustomerRegistrationTrend(
                            key,
                            customers
                    );
                })
                .toList();
    }

    private List<CustomerRegistrationTrend> getDailyRegistrationTrend(
            DashboardDateRange range) {

        List<Object[]> results =
                customerRepository.getDailyRegistrationTrend(
                        range.start(),
                        range.end()
                );

        Map<String, Long> databaseValues =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        row -> (String) row[0],
                                        row -> ((Number) row[1])
                                                .longValue()
                                )
                        );

        List<LocalDate> days =
                dashboardTimelineGenerator.generateDays(
                        range.start(),
                        range.end()
                );

        return days.stream()
                .map(day -> {
                    String key =
                            day.toString();

                    long customers =
                            databaseValues.getOrDefault(
                                    key,
                                    0L
                            );

                    return new CustomerRegistrationTrend(
                            key,
                            customers
                    );
                })
                .toList();
    }

}
