package com.loan_manager_app.loans_manager.LOANS.servImpl;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLookupService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessedResponse;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessingInfo;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequests;
import com.loan_manager_app.loans_manager.LOANS.loan_api.InitializeRepaymentPlanEvent;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.LoanAmountExceedsBorrowedAmountException;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.LoanNotFoundException;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanCountUpdateEvent;
import com.loan_manager_app.loans_manager.LOANS.modals.Loan;
import com.loan_manager_app.loans_manager.LOANS.repos.LoanRepository;
import com.loan_manager_app.loans_manager.LOANS.service.LoanService;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfoExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CustomerLookupService customerLookupService;
    private final DeviceInfoExtractor deviceInfoExtractor;

    @Override
    public LoanRequests getLoanById(Long loanId) {
        Loan loanToGet = loanRepository.findById(loanId)
                .orElseThrow(()-> new LoanNotFoundException("Loan record not found" +
                        "!"));
        return mapToLoanRequest(loanToGet);
    }

    @Override
    public PageResponse<LoanRequests> getAllLoans(int page, int size) {

        Page<Loan> loanPage =
                loanRepository.findAll(PageRequest.of(page, size));

        List<LoanRequests> loanRequestList = loanPage.getContent()
                .stream()
                .map(this::mapToLoanRequest)
                .toList();

        return PageResponse.<LoanRequests>builder()
                .content(loanRequestList)
                .page(loanPage.getNumber())
                .size(loanPage.getSize())
                .totalElements(loanPage.getTotalElements())
                .totalPages(loanPage.getTotalPages())
                .first(loanPage.isFirst())
                .last(loanPage.isLast())
                .build();
    }

    @Transactional
    @Override
    public LoanProcessedResponse processLoan(LoanProcessingInfo loanProcessingInfo) {

        Loan loanToProcess = loanRepository
                .findById(loanProcessingInfo.getLoanId())
                .orElseThrow(() ->
                        new LoanNotFoundException(
                                "Loan not found with id: "
                                        + loanProcessingInfo.getLoanId()
                        )
                );

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        BigDecimal initialBorrowedAmount = loanToProcess.getLoanAmountBorrowed();

        BigDecimal amountToIssue = loanProcessingInfo.getLoanAmountToIssue();

        BigDecimal interestRate = loanProcessingInfo.getInterestRate();

        int durationInMonths = loanProcessingInfo.getDurationInMonths();

        // VALIDATE AMOUNT TO ISSUE

        if (amountToIssue == null || amountToIssue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount to issue must be greater than zero"
            );
        }

        if (amountToIssue.compareTo(initialBorrowedAmount) > 0) {
            throw new LoanAmountExceedsBorrowedAmountException(
                    "Loan amount to issue exceeds the amount originally requested"
            );
        }

        // VALIDATE AGAINST CUSTOMER INCOME
        if (amountToIssue.compareTo(loanToProcess.getCustomerMonthlyIncome()) > 0) {
            throw new RuntimeException(
                    "Loan amount to issue is greater than customer's monthly income"
            );
        }


        // VALIDATE INTEREST RATE
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Interest rate cannot be negative"
            );
        }


        // VALIDATE LOAN DURATION
        if (durationInMonths <= 0) {
            throw new IllegalArgumentException(
                    "Loan duration must be greater than zero"
            );
        }


        // CALCULATE INTEREST
        BigDecimal interest = calculateInterest(
                amountToIssue,
                interestRate,
                durationInMonths
        );


        // CALCULATE TOTAL REPAYMENT
        BigDecimal totalLoanToRepay = amountToIssue.add(interest);


        // UPDATE LOAN
        loanToProcess.setLoanAmountBorrowed(amountToIssue);
        loanToProcess.setLoanDuration(durationInMonths);
        loanToProcess.setLoanAmountIssued(amountToIssue);
        loanToProcess.setInterestRate(interest);
        loanToProcess.setTotalAmountToRepay(totalLoanToRepay);
        loanToProcess.setLoanPurpose(loanProcessingInfo.getLoanPurpose());
        loanToProcess.setLoanStatus(LoanStatus.APPROVED);
        loanToProcess.setProcessedAt(LocalDateTime.now());

        Loan processedLoan = loanRepository.save(loanToProcess);

        //update customer loan count
        eventPublisher.publishEvent(
                new LoanCountUpdateEvent(
                        processedLoan.getCustomerId()
                )
        );

        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "approved loan request",
                        LogType.LOAN_APPROVED,
                        deviceInfo
                )
        );

        eventPublisher.publishEvent(
                new InitializeRepaymentPlanEvent(
                        processedLoan.getId(),
                        processedLoan.getCustomerId(),
                        processedLoan.getLoanAmountIssued(),
                        processedLoan.getTotalAmountToRepay(),
                        processedLoan.getLoanDuration()
                )
        );


        // RESPONSE
        return LoanProcessedResponse.builder()
                .message(
                        "Loan request processed successfully. "
                                + "The amount requested will be credited to customer's "
                                + "account within 24 hours"
                )
                .loanTermInMonths(processedLoan.getLoanDuration())
                .amountIssued(processedLoan.getLoanAmountIssued())
                .interestRateIssued(processedLoan.getInterestRate())
                .totalAmountToRepay(processedLoan.getTotalAmountToRepay())
                .build();
    }

    private static BigDecimal calculateInterest(
            BigDecimal amountToIssue,
            BigDecimal interestRate,
            int durationInMonths
    ) {

        return amountToIssue
                .multiply(interestRate)
                .multiply(BigDecimal.valueOf(durationInMonths))
                .divide(BigDecimal.valueOf(100));
    }


    private LoanRequests mapToLoanRequest(Loan loan) {

        CustomerSummary customerSummary =
                customerLookupService.getCustomerSummary(loan.getCustomerId());

        BigDecimal interest = loan.getInterestRate()
                .multiply(loan.getInterestRate())
                .multiply(BigDecimal.valueOf(loan.getLoanDuration()))
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalLoan = loan.getLoanAmountBorrowed().add(interest);

        return LoanRequests.builder()
                .loanId(loan.getId())
                .customerInformation(customerSummary)
                .loanPurpose(loan.getLoanPurpose())
                .loanInterestRate(loan.getInterestRate())
                .loanAmountRequested(loan.getLoanAmountBorrowed())
                .loanAmountIssued(loan.getLoanAmountIssued())
                .loanStatus(loan.getLoanStatus().toString())
                .loanDuration(loan.getLoanDuration())
                .totalAmountToRepay(totalLoan)
                .build();
    }
}
