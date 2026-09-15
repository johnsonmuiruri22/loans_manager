package com.loan_manager_app.loans_manager.SHARED.deviceInfo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {
    private String ipAddress;
    private String operatingSystem;
    private String deviceName;
    private String deviceType;
}
