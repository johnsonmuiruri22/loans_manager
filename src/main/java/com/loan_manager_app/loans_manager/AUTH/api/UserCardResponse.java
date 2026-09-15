package com.loan_manager_app.loans_manager.AUTH.api;


import lombok.Builder;

@Builder
public record UserCardResponse(
        Long userId,
        String surname,
        String otherNames,
        String username,
        String userRole
) {
}
