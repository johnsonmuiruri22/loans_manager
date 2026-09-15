package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service;

import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.PaymentRecordDTO;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.RepaymentRequest;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;

public interface LoanRepaymentService {
    PageResponse<PaymentRecordDTO> getPaymentRecords(int page, int size);
    void initializePayment(Long loanId);
    PaymentRecordDTO pay(RepaymentRequest request);
}
