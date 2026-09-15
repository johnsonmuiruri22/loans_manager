package com.loan_manager_app.loans_manager.dashboard.utils;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Component
public class DashboardTimelineGenerator {
    public List<YearMonth> generateMonths(
            LocalDateTime start,
            LocalDateTime end) {

        YearMonth first =
                YearMonth.from(start);

        YearMonth last =
                YearMonth.from(end.minusNanos(1));

        List<YearMonth> months =
                new ArrayList<>();
        YearMonth current = first;

        while (!current.isAfter(last)) {
            months.add(current);
            current = current.plusMonths(1);
        }

        return months;
    }

    public List<LocalDate> generateDays(
            LocalDateTime start,
            LocalDateTime end) {
        LocalDate first =
                start.toLocalDate();

        LocalDate last =
                end.minusNanos(1)
                        .toLocalDate();

        List<LocalDate> days =
                new ArrayList<>();
        LocalDate current = first;
        while (!current.isAfter(last)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }
}
