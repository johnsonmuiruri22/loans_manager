package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;

public record CustomerCreatedEvent(
        Long customerId,
        String customerSurname,
        String customerOtherNames,
        String customerEmail
) {
}
