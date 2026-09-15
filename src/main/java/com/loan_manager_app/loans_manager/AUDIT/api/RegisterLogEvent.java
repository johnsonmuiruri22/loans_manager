package com.loan_manager_app.loans_manager.AUDIT.api;

import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;


public record RegisterLogEvent(
        String activity,
        LogType logType,
        DeviceInfo deviceInfo
) {
}
