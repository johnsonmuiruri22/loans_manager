package com.loan_manager_app.loans_manager.LOANS.servImpl;


import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanDashboardQuery;
import com.loan_manager_app.loans_manager.LOANS.repos.LoanRepository;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import com.loan_manager_app.loans_manager.dashboard.chartDTOs.LoanBorrowingTrend;
import com.loan_manager_app.loans_manager.dashboard.dto.DashboardDateRange;
import com.loan_manager_app.loans_manager.dashboard.enums.DashboardPeriod;
import com.loan_manager_app.loans_manager.dashboard.utils.DashboardPeriodCalculator;
import com.loan_manager_app.loans_manager.dashboard.utils.DashboardTimelineGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanDashboardQueryServImpl implements LoanDashboardQuery {
    private final LoanRepository loanRepository;
    private final DashboardPeriodCalculator dashboardPeriodCalculator;
    private final DashboardTimelineGenerator dashboardTimelineGenerator;

    @Override
    public long countLoans() {
        return loanRepository.count();
    }

    @Override
    public long countActiveLoans() {
        return loanRepository.countByLoanStatus(LoanStatus.APPROVED);
    }

    @Override
    public BigDecimal getTotalAmountBorrowed() {
        BigDecimal amount =
                loanRepository.getTotalAmountBorrowed();

        return amount != null
                ? amount
                : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalAmountIssued() {
        BigDecimal amount =
                loanRepository.getTotalAmountIssued();

        return amount != null
                ? amount
                : BigDecimal.ZERO;
    }

    @Override
    public List<LoanBorrowingTrend> getBorrowingTrend(
            DashboardPeriod period) {
        DashboardDateRange range =
                dashboardPeriodCalculator.calculate(period);

        if (period == DashboardPeriod.LAST_7_DAYS ||
                period == DashboardPeriod.LAST_30_DAYS) {
            return getDailyBorrowingTrend(range);
        }
        return getMonthlyBorrowingTrend(range);
    }

    private List<LoanBorrowingTrend> getDailyBorrowingTrend(
            DashboardDateRange range) {
        List<Object[]> results =
                loanRepository.getDailyBorrowingTrend(
                        range.start(),
                        range.end()
                );

        Map<String, BigDecimal> databaseValues =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        row -> (String) row[0],
                                        row -> (BigDecimal) row[1]
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

                    BigDecimal amount =
                            databaseValues.getOrDefault(
                                    key,
                                    BigDecimal.ZERO
                            );

                    return new LoanBorrowingTrend(
                            key,
                            amount
                    );
                })
                .toList();
    }

    private List<LoanBorrowingTrend> getMonthlyBorrowingTrend(
            DashboardDateRange range) {
        List<Object[]> results =
                loanRepository.getBorrowingTrend(
                        range.start(),
                        range.end()
                );

        Map<String, BigDecimal> databaseValues =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        row -> (String) row[0],
                                        row -> (BigDecimal) row[1]
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

                    BigDecimal amount =
                            databaseValues.getOrDefault(
                                    key,
                                    BigDecimal.ZERO
                            );

                    return new LoanBorrowingTrend(
                            key,
                            amount
                    );
                })
                .toList();
    }
}
