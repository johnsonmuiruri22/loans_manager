package com.loan_manager_app.loans_manager.AUDIT.service;

import com.loan_manager_app.loans_manager.AUDIT.dto.LogResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;

public interface AuditService {
    PageResponse<LogResponse> getLogs(int page, int size);
}
