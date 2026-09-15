package com.loan_manager_app.loans_manager.AUDIT.dto;


import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogResponse {
    private Long logId;
    private String activity;
    private String logType;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String operatingSystem;
    private String deviceName;
    private String deviceType;
    private String userAgent;
}
