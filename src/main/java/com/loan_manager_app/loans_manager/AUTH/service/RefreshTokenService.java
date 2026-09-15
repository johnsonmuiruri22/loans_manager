package com.loan_manager_app.loans_manager.AUTH.service;

import com.loan_manager_app.loans_manager.AUTH.events.RefreshTokenSession;
import com.loan_manager_app.loans_manager.AUTH.modals.RefreshToken;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.SHARED.GlobalEvents.ClientInfo;

public interface RefreshTokenService {
    RefreshTokenSession createSession(
            Users user,
            String device,
            String ipAddress
    );
    RefreshToken verify(String rawRefreshToken);
    RefreshTokenSession rotate(String rawRefreshToken, ClientInfo clientInfo);
    void revoke(String rawToken);
    int revokeAll(Users user);
    void deleteExpiredSessions();
    void handleInvalidStatus(
            RefreshToken refreshToken
    );
}
