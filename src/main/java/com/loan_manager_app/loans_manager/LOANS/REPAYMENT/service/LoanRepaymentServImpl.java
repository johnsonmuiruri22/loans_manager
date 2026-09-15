package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLookupService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.PaymentRecordDTO;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.RepaymentRequest;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.modal.LoanRepayment;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.repo.RepaymentRepository;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanLookupService;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanSummary;
import com.loan_manager_app.loans_manager.LOANS.loan_api.UpdateCustomerLoanPaid;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.RepaymentStatus;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfoExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanRepaymentServImpl implements LoanRepaymentService {
    private final RepaymentRepository repository;
    private final CustomerLookupService customerLookupService;
    private final LoanLookupService loanLookupService;
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceInfoExtractor deviceInfoExtractor;

    @Override
    public PageResponse<PaymentRecordDTO> getPaymentRecords(int page, int size) {
        Page<LoanRepayment> repaymentPage = repository
                .findAll(PageRequest.of(page, size));

        List<PaymentRecordDTO> repaymentList = repaymentPage
                .getContent()
                .stream()
                .map(
                        record -> PaymentRecordDTO
                                .builder()
                                .message("Retrieved a total of " +
                                        repaymentPage.getTotalElements() + " payment records")
                                .repaymentId(record.getId())
                                .customerFullName(customerLookupService
                                        .getCustomerSummary(record.getCustomerId())
                                        .getCustomerFullName())
                                .amountPaid(record.getAmountPaid())
                                .totalAmountToBeRepaid(record.getTotalAmountToBeRepaid())
                                .remainingBalance(record.getRemainingBalance())
                                .repaidAt(record.getRepaidAt())
                                .repaymentStatus(record.getRepaymentStatus().toString())
                                .build()
                )
                .toList();
        return PageResponse.<PaymentRecordDTO>builder()
                .content(repaymentList)
                .totalPages(repaymentPage.getTotalPages())
                .totalElements(repaymentPage.getTotalElements())
                .size(repaymentPage.getSize())
                .first(repaymentPage.isFirst())
                .last(repaymentPage.isLast())
                .build();
    }


    @Transactional
    @Override
    public void initializePayment(Long loanId) {
        LoanSummary loanSummary =
                loanLookupService.getLoanSummary(loanId);
        LoanRepayment loanRepayment = getLoanRepayment(loanId, loanSummary);
        repository.save(loanRepayment);
        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "Loan repayment schedule has been initialized",
                        LogType.LOAN_REPAYMENT_INITIALIZED,
                        deviceInfo
                )
        );
    }

    @Transactional
    @Override
    public PaymentRecordDTO pay(RepaymentRequest request) {
        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        // 1. Validate request
        if (request == null || request.getRepaymentId() == null) {
            throw new IllegalArgumentException("Repayment ID is required");
        }

        if (request.getAmountToPay() == null ||
                request.getAmountToPay().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        // 2. Find the repayment record
        LoanRepayment loanRepaymentToPay = repository
                .findById(request.getRepaymentId())
                .orElseThrow(() ->
                        new RuntimeException("Loan repayment not found")
                );

        // 3. Get loan information
        LoanSummary loanSummary = loanLookupService
                .getLoanSummary(loanRepaymentToPay.getLoanId());

        // 4. Get payment amount
        BigDecimal amountToPay = request.getAmountToPay();

        // 5. Get amount already paid
        BigDecimal currentAmountPaid =
                loanRepaymentToPay.getAmountPaid() == null
                        ? BigDecimal.ZERO
                        : loanRepaymentToPay.getAmountPaid();

        // 6. Calculate new total amount paid
        BigDecimal newAmountPaid = currentAmountPaid.add(amountToPay);

        // 7. Get the total amount that should be repaid
        BigDecimal totalAmountToBeRepaid =
                loanRepaymentToPay.getTotalAmountToBeRepaid();

        // If it hasn't been set yet, get it from the loan summary
        if (totalAmountToBeRepaid == null) {
            totalAmountToBeRepaid = loanSummary.totalAmountToBeRepaid();
        }

        // 8. Calculate the remaining balance
        BigDecimal remainingBalance =
                totalAmountToBeRepaid.subtract(newAmountPaid);

        // 9. Prevent overpayment
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Payment exceeds the remaining loan balance"
            );
        }

        // 10. Update repayment information
        loanRepaymentToPay.setAmountPaid(newAmountPaid);
        loanRepaymentToPay.setTotalAmountToBeRepaid(totalAmountToBeRepaid);
        loanRepaymentToPay.setRemainingBalance(remainingBalance);

        // 11. Update repayment status
        loanRepaymentToPay.setRepaymentStatus(
                remainingBalance.compareTo(BigDecimal.ZERO) == 0
                        ? RepaymentStatus.PAID_OFF
                        : RepaymentStatus.IN_PROGRESS
        );

        // 12. Set customer information from the loan
        loanRepaymentToPay.setCustomerId(loanSummary.customerId());
        loanRepaymentToPay.setAmountIssuedToCustomer(
                loanSummary.loanAmountIssued()
        );

        // 13. Save repayment
        LoanRepayment paidLoan = repository.save(loanRepaymentToPay);

        // 14. Get the customer's name
        String customerFullName = customerLookupService
                .getCustomerSummary(paidLoan.getCustomerId())
                .getCustomerFullName();

        if (paidLoan.getRemainingBalance().compareTo(BigDecimal.ZERO) == 0){
            eventPublisher.publishEvent(
                   new UpdateCustomerLoanPaid(
                           paidLoan.getCustomerId()
                   )
            );
        }

        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "Loan repayment activity has been completed",
                        LogType.LOAN_REPAYMENT,
                        deviceInfo
                )
        );

        // 15. Return payment response
        return PaymentRecordDTO.builder()
                .repaymentId(paidLoan.getId())
                .customerFullName(customerFullName)
                .amountPaid(paidLoan.getAmountPaid())
                .totalAmountToBeRepaid(paidLoan.getTotalAmountToBeRepaid())
                .remainingBalance(paidLoan.getRemainingBalance())
                .repaidAt(LocalDateTime.now())
                .repaymentStatus(
                        paidLoan.getRepaymentStatus().toString()
                )
                .message("We have received your payment of " + paidLoan.getAmountPaid()
                +" Ksh. Thank you.")
                .build();
    }

    private static @NonNull LoanRepayment getLoanRepayment(Long loanId, LoanSummary loanSummary) {
        LoanRepayment loanRepayment =
                new LoanRepayment();
        loanRepayment.setLoanId(loanId);
        loanRepayment.setCustomerId(loanSummary.customerId());
        loanRepayment.setRepaymentStatus(RepaymentStatus.IN_PROGRESS);
        loanRepayment.setAmountPaid(new BigDecimal(0));
        loanRepayment.setAmountIssuedToCustomer(loanSummary.loanAmountIssued());
        loanRepayment.setTotalAmountToBeRepaid(loanSummary.totalAmountToBeRepaid());
        loanRepayment.setRemainingBalance(
                loanRepayment.getTotalAmountToBeRepaid().subtract(loanRepayment.getAmountPaid())
        );
        return loanRepayment;
    }


}
