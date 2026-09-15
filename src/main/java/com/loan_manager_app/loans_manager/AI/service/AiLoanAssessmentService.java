package com.loan_manager_app.loans_manager.AI.service;

import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentRequest;
import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentResponse;

public interface AiLoanAssessmentService {
    LoanAiAssessmentResponse assessLoan(
            LoanAiAssessmentRequest request
    );

    LoanAiAssessmentResponse assessLoan(
            Long loanId
    );
    LoanAiAssessmentResponse getAssessment(Long loanId);
}
