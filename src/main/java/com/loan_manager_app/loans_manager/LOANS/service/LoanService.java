package com.loan_manager_app.loans_manager.LOANS.service;

import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessedResponse;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessingInfo;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequests;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;

public interface LoanService {
   LoanRequests getLoanById(Long loanId);
   PageResponse<LoanRequests> getAllLoans(int page, int size);
   LoanProcessedResponse processLoan(LoanProcessingInfo loanProcessingInfo);
}
