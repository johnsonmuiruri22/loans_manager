package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;

public interface CustomerLookupService {
    CustomerSummary getCustomerSummary(Long customerId);
    boolean hasActiveLoanRequest(Long customerId);
}
