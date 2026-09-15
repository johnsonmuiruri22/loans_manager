package com.loan_manager_app.loans_manager.AUDIT.service;


import com.loan_manager_app.loans_manager.AUDIT.dto.LogResponse;
import com.loan_manager_app.loans_manager.AUDIT.modal.Audit;
import com.loan_manager_app.loans_manager.AUDIT.repo.AuditRepository;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    @Override
    public PageResponse<LogResponse> getLogs(int page, int size) {
        Page<Audit> auditPage =
                auditRepository.findAll(
                        PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                        Sort.Direction.DESC,
                                        "timestamp"
                                )
                        )
                );

        List<LogResponse> logsResponse = auditPage
                .getContent()
                .stream()
                .map(
                        audit -> LogResponse.builder()
                                .logId(audit.getId())
                                .logType(audit.getLogType().toString())
                                .activity(audit.getActivity())
                                .timestamp(audit.getTimestamp())
                                .ipAddress(audit.getIpAddress())
                                .deviceName(audit.getDeviceName())
                                .userAgent(audit.getUserAgent())
                                .deviceType(audit.getDeviceType())
                                .operatingSystem(audit.getOperatingSystem())
                                .build()
                ).toList();
        return PageResponse.<LogResponse>builder()
                .content(logsResponse)
                .page(auditPage.getNumber())
                .totalElements(auditPage.getTotalElements())
                .totalPages(auditPage.getTotalPages())
                .size(auditPage.getSize())
                .first(auditPage.isFirst())
                .last(auditPage.isLast())
                .build();
    }

}
