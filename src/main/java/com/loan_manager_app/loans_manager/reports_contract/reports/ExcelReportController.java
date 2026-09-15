package com.loan_manager_app.loans_manager.reports_contract.reports;


import com.loan_manager_app.loans_manager.AUTH.api.UserExcelService;
import com.loan_manager_app.loans_manager.SHARED.exports.service.AuditLogExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.service.RepaymentExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.service.CustomerExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.service.LoanExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan-management/reports")
@RequiredArgsConstructor
public class ExcelReportController {
    private final CustomerExcelExportService customerExcelExportService;
    private final LoanExcelExportService loanExcelExportService;
    private final RepaymentExcelExportService repaymentExcelExportService;
    private final AuditLogExcelExportService auditLogExcelExportService;
    private final UserExcelService userExcelService;

    @GetMapping("/customers/excel")
    public ResponseEntity<byte[]> exportCustomers() {

        byte[] file = customerExcelExportService.exportCustomers();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"customers.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @GetMapping("/loans/excel")
    public ResponseEntity<byte[]> exportLoans() {

        byte[] file =
                loanExcelExportService.exportLoans();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"loans.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @GetMapping("/repayments/excel")
    public ResponseEntity<byte[]> exportRepayments() {

        byte[] file =
                repaymentExcelExportService.exportRepayments();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"repayments.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @GetMapping("/audit-logs/excel")
    public ResponseEntity<byte[]> exportAuditLogs() {

        byte[] file =
                auditLogExcelExportService.exportAuditLogs();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"audit-logs.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @GetMapping("/users/excel")
    public ResponseEntity<byte[]> exportUsers(){
        byte[]usersFile =
                userExcelService.exportUsersToExcel();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"users.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(usersFile);
    }

}
