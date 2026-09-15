package com.loan_manager_app.loans_manager.AUTH.events;

public record CurrentUserResponse(
        Long id,
        String username,
        String fullName,
        String role,
        boolean authenticated
) {
}
