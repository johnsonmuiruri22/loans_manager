package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;

public interface CustomerLoanStateService {
    void markLoanRequested(Long customerId);
    void markLoanRepaid(Long customerId);
    void incrementLoanCount(Long customerId);
}
