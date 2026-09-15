package com.loan_manager_app.loans_manager.AUDIT.service;


import com.loan_manager_app.loans_manager.AUDIT.dto.LogResponse;
import com.loan_manager_app.loans_manager.AUDIT.modal.Audit;
import com.loan_manager_app.loans_manager.AUDIT.repo.AuditRepository;
import com.loan_manager_app.loans_manager.SHARED.exports.service.AuditLogExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.utils.ExcelWorkbookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditExcelExportServImpl implements AuditLogExcelExportService {
    private final AuditRepository auditRepository;

    @Override
    public byte[] exportAuditLogs() {
        List<Audit> auditLogs = auditRepository.findAll();

        List<LogResponse> auditList = auditLogs.stream()
                .map(audit -> LogResponse.builder()
                        .logId(audit.getId())
                        .logType(audit.getLogType().toString())
                        .activity(audit.getActivity())
                        .timestamp(audit.getTimestamp())
                        .ipAddress(audit.getIpAddress())
                        .operatingSystem(audit.getOperatingSystem())
                        .deviceName(audit.getDeviceName())
                        .deviceType(audit.getDeviceType())
                        .build())
                .toList();

        String [] headers = {
                "ID",
                "Log Type",
                "Activity",
                "Timestamp",
                "Ip Address",
                "Operating System",
                "Device Name",
                "Device Type"
        };

        List<Object[]> rows = auditList
                .stream()
                .map(
                        record -> new Object[]{
                                record.getLogId(),
                                record.getLogType(),
                                record.getActivity(),
                                record.getTimestamp(),
                                record.getIpAddress(),
                                record.getOperatingSystem(),
                                record.getDeviceName(),
                                record.getDeviceType()
                        }
                )
                .toList();


        return ExcelWorkbookUtil.createWorkbook(
                "Audit Logs",
                headers,
                rows
        );
    }
}
