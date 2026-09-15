package com.loan_manager_app.loans_manager.SHARED.GlobalEvents;

public record ClientInfo(
        String ipAddress,
        String userAgent,
        String deviceName
) {
}
