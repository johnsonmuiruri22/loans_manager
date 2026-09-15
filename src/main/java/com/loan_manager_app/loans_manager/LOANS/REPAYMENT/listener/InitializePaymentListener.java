package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.listener;


import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service.LoanRepaymentService;
import com.loan_manager_app.loans_manager.LOANS.loan_api.InitializeRepaymentPlanEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializePaymentListener {
    private final LoanRepaymentService loanRepaymentService;

    @EventListener
    public void initializeRepaymentPlan(InitializeRepaymentPlanEvent event) {
        loanRepaymentService.initializePayment(event.loanId());
    }
}
