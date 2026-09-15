package com.loan_manager_app.loans_manager.LOANS.servImpl;


import com.loan_manager_app.loans_manager.LOANS.modals.Loan;
import com.loan_manager_app.loans_manager.LOANS.repos.LoanRepository;
import com.loan_manager_app.loans_manager.SHARED.exports.service.LoanExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.utils.ExcelWorkbookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanExcelExportServImpl implements LoanExcelExportService {
    private final LoanRepository loanRepository;

    @Override
    public byte[] exportLoans() {
        List<Loan> loans = loanRepository.findAll();

        String[] headers = {
                "Loan ID",
                "Customer ID",
                "Amount Requested",
                "Amount Issued",
                "Loan Duration",
                "Interest Amount",
                "Total Amount To Repay",
                "Loan Status"
        };

        List<Object[]> rows = loans.stream()
                .map(loan -> new Object[]{

                        loan.getId(),
                        loan.getCustomerId(),
                        loan.getLoanAmountBorrowed(),
                        loan.getLoanAmountIssued(),
                        loan.getLoanDuration(),
                        loan.getInterestRate(),
                        loan.getTotalAmountToRepay(),
                        loan.getLoanStatus()

                })
                .toList();

        return ExcelWorkbookUtil.createWorkbook(
                "Loans",
                headers,
                rows
        );
    }
}
