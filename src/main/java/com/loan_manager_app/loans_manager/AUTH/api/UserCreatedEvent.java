package com.loan_manager_app.loans_manager.AUTH.api;

public record UserCreatedEvent(
        String username,
        String temporaryPassword,
        String email
) {
}
