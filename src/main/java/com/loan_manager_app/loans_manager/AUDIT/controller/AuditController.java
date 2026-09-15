package com.loan_manager_app.loans_manager.AUDIT.controller;


import com.loan_manager_app.loans_manager.AUDIT.dto.LogResponse;
import com.loan_manager_app.loans_manager.AUDIT.service.AuditService;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/logs")
    public ResponseEntity<PageResponse<LogResponse>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(auditService.getLogs(page, size));
    }
}
