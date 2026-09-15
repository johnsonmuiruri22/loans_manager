package com.loan_manager_app.loans_manager.AUTH.events;

import com.loan_manager_app.loans_manager.AUTH.modals.RefreshToken;

public record RefreshTokenSession(
        String rawToken,
        RefreshToken refreshToken
) {
}
