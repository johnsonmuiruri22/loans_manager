package com.loan_manager_app.loans_manager.LOANS.servImpl;


import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanLookupService;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanSummary;
import com.loan_manager_app.loans_manager.LOANS.modals.Loan;
import com.loan_manager_app.loans_manager.LOANS.repos.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanLookupServImpl implements LoanLookupService {
    private final LoanRepository loanRepository;

    @Override
    public LoanSummary getLoanSummary(Long loanId) {
        Loan loanToGet = loanRepository.findById(loanId)
                .orElseThrow(()->new RuntimeException("Loan not found"));
        return LoanSummary.builder()
                .customerId(loanToGet.getCustomerId())
                .loanDuration(loanToGet.getLoanDuration())
                .loanAmountIssued(loanToGet.getLoanAmountIssued())
                .totalAmountToBeRepaid(loanToGet.getTotalAmountToRepay())
                .loanAmountRequested(loanToGet.getLoanAmountBorrowed())
                .build();
    }
}
