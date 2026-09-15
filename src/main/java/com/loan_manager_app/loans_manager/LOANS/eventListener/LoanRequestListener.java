package com.loan_manager_app.loans_manager.LOANS.eventListener;


import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanRequestCreatedEvent;
import com.loan_manager_app.loans_manager.LOANS.modals.Loan;
import com.loan_manager_app.loans_manager.LOANS.repos.LoanRepository;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LoanRequestListener {
    private final LoanRepository loanRepository;

    @ApplicationModuleListener
    @Transactional
    public void onLoanRequestCreated(LoanRequestCreatedEvent loanRequestCreatedEvent){
        Loan newLoanRequest = new Loan();
        newLoanRequest.setCustomerId(loanRequestCreatedEvent.customerId());
        newLoanRequest.setLoanStatus(LoanStatus.PENDING);
        newLoanRequest.setLoanDuration(loanRequestCreatedEvent.durationToRepay());
        newLoanRequest.setLoanAmountBorrowed(loanRequestCreatedEvent.amountRequested());
        newLoanRequest.setLoanPurpose(loanRequestCreatedEvent.loanPurpose());
        newLoanRequest.setCustomerMonthlyIncome(loanRequestCreatedEvent.customerIncomePerMonth());
        newLoanRequest.setLoanAmountIssued(new BigDecimal(0));
        newLoanRequest.setInterestRate(new BigDecimal(0));
        newLoanRequest.setTotalAmountToRepay(new BigDecimal(0));
        loanRepository.save(newLoanRequest);
    }
}
