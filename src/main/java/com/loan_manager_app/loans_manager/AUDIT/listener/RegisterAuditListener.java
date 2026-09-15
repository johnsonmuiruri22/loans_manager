package com.loan_manager_app.loans_manager.AUDIT.listener;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.AUDIT.modal.Audit;
import com.loan_manager_app.loans_manager.AUDIT.repo.AuditRepository;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegisterAuditListener {
    private final AuditRepository auditRepository;

    @ApplicationModuleListener
    @Transactional
    public void onLogCreated(RegisterLogEvent registerLogEvent){
        Audit newLog = new Audit();
        newLog.setLogType(registerLogEvent.logType());
        newLog.setActivity(registerLogEvent.activity());
        newLog.setTimestamp(LocalDateTime.now());

        DeviceInfo deviceInfo = registerLogEvent.deviceInfo();
        if (deviceInfo != null) {
            newLog.setIpAddress(deviceInfo.getIpAddress());
            newLog.setOperatingSystem(
                    deviceInfo.getOperatingSystem()
            );
            newLog.setDeviceName(
                    deviceInfo.getDeviceName()
            );
            newLog.setDeviceType(
                    deviceInfo.getDeviceType()
            );
        }
        auditRepository.save(newLog);
    }
}
