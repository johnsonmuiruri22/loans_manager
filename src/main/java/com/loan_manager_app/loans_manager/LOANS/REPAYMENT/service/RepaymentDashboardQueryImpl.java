package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service;


import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.repo.RepaymentRepository;
import com.loan_manager_app.loans_manager.LOANS.loan_api.RepaymentDashboardQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RepaymentDashboardQueryImpl implements RepaymentDashboardQuery {
    private final RepaymentRepository repaymentRepository;

    @Override
    public BigDecimal getTotalAmountPaid() {
        BigDecimal totalAmount =
                repaymentRepository.getTotalAmountPaid();

        return totalAmount != null ?
                totalAmount:BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getOutstandingBalance() {
        BigDecimal outstandingBalance =
                repaymentRepository.getOutstandingBalance();

        return outstandingBalance != null ?
                outstandingBalance:BigDecimal.ZERO;
    }
}
