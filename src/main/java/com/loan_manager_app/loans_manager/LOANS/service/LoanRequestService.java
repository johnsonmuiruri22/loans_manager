package com.loan_manager_app.loans_manager.LOANS.service;

import com.loan_manager_app.loans_manager.LOANS.DTOS.CustomerLoanRequest;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequestDTO;

public interface LoanRequestService {
    CustomerLoanRequest requestLoan(LoanRequestDTO loanRequestDTO);
}
