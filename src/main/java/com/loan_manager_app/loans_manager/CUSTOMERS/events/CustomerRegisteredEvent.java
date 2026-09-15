package com.loan_manager_app.loans_manager.CUSTOMERS.events;

public record CustomerRegisteredEvent(
        Long customerId,
        String customerFullName,
        String customerEmail
) {
}
