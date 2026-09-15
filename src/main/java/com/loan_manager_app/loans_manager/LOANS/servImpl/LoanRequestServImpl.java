package com.loan_manager_app.loans_manager.LOANS.servImpl;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLoanStateService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLookupService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import com.loan_manager_app.loans_manager.LOANS.DTOS.CustomerLoanRequest;
import com.loan_manager_app.loans_manager.LOANS.service.LoanRequestService;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequestDTO;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanRequestCreatedEvent;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfoExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanRequestServImpl implements LoanRequestService {
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceInfoExtractor deviceInfoExtractor;
    private final CustomerLookupService customerLookupService;
    private final CustomerLoanStateService customerLoanStateService;


    @Transactional
    @Override
    public CustomerLoanRequest requestLoan(LoanRequestDTO loanRequestDTO) {
        Long customerId = loanRequestDTO.getCustomerId();

        //get customer
        CustomerSummary customer = customerLookupService
                .getCustomerSummary(customerId);

        //check whether customer has an existing loan request
        boolean customerHasExistingLoan =
                customerLookupService.hasActiveLoanRequest(customerId);

        if (customerHasExistingLoan) {
            throw new RuntimeException("Customer already has an existing loan request");
        }

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();

        BigDecimal amountRequested =
                loanRequestDTO.getLoanAmount();

        String purpose =
                loanRequestDTO.getLoanPurpose();

        int durationToRepay =
                loanRequestDTO.getDuration();

        LocalDateTime requestedAt =
                loanRequestDTO.getRequestedAt() != null ?
                        loanRequestDTO.getRequestedAt() : LocalDateTime.now();

        // 3. Publish the loan event from the LOANS module
        eventPublisher.publishEvent(
                new LoanRequestCreatedEvent(
                        customerId,
                        customer.getCustomerFullName(),
                        amountRequested,
                        purpose,
                        customer.getCreditStatus(),
                        customer.getMonthlyIncome(),
                        durationToRepay,
                        requestedAt
                )
        );
        // 4. Tell CUSTOMERS to update its own state
        customerLoanStateService.markLoanRequested(customerId);

        //write audit log
        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "Loan request has been initiated",
                        LogType.LOAN_REQUESTED,
                        deviceInfo
                )
        );

        // 6. Return response
        return CustomerLoanRequest.builder()
                .message(
                        "Loan request has been received and is being processed"
                )
                .customerId(customerId)
                .customerFullName(customer.getCustomerFullName())
                .customerCreditStatus(customer.getCreditStatus())
                .customerEmail(customer.getEmail()) // we'll address this below
                .loanAmount(amountRequested)
                .durationInMonths(durationToRepay)
                .loanPurpose(purpose)
                .requestedAt(requestedAt)
                .build();
    }
}
