package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service;


import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.modal.LoanRepayment;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.repo.RepaymentRepository;
import com.loan_manager_app.loans_manager.SHARED.exports.service.RepaymentExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.utils.ExcelWorkbookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepaymentExcelExportServImpl implements RepaymentExcelExportService {
    private final RepaymentRepository repaymentRepository;

    @Override
    public byte[] exportRepayments() {
        List<LoanRepayment> repayments = repaymentRepository.findAll();
        String[] headers = {
                "Repayment ID",
                "Customer ID",
                "Loan ID",
                "Amount Issued",
                "Total Amount To Be Repaid",
                "Amount Paid",
                "Remaining Balance",
                "Repayment Status"
        };

        List<Object[]> rows = repayments.stream()
                .map(repayment -> new Object[]{

                        repayment.getId(),
                        repayment.getCustomerId(),
                        repayment.getLoanId(),
                        repayment.getAmountIssuedToCustomer(),
                        repayment.getTotalAmountToBeRepaid(),
                        repayment.getAmountPaid(),
                        repayment.getRemainingBalance(),
                        repayment.getRepaymentStatus()

                })
                .toList();

        return ExcelWorkbookUtil.createWorkbook(
                "Repayments",
                headers,
                rows
        );
    }
}
