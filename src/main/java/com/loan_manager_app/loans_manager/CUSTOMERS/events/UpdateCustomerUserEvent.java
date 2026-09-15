package com.loan_manager_app.loans_manager.CUSTOMERS.events;

public record UpdateCustomerUserEvent(
        String surname,
        String otherNames,
        String userEmail
) {
}
