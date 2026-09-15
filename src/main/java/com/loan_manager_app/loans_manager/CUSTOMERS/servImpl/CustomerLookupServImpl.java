package com.loan_manager_app.loans_manager.CUSTOMERS.servImpl;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLookupService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import com.loan_manager_app.loans_manager.CUSTOMERS.repo.CustomerRepository;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLookupServImpl implements CustomerLookupService {
    private final CustomerRepository customerRepository;
    @Override
    public CustomerSummary getCustomerSummary(Long customerId) {
        Customer customerToGet = customerRepository
                .findByCustomerId(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return CustomerSummary.builder()
                .phoneNumber(customerToGet.getPhone())
                .address(customerToGet.getResidence())
                .email(customerToGet.getEmail())
                .customerFullName(customerToGet.getFullName())
                .creditStatus(customerToGet.getCreditStatus().toString())
                .monthlyIncome(customerToGet.getMonthlyIncome())
                .loanCount(customerToGet.getLoanCount())
                .build();
    }

    @Override
    public boolean hasActiveLoanRequest(Long customerId) {
        return customerRepository.
                existsByCustomerIdAndHasRequestedLoanTrueAndHasRepaidLoanFalse(customerId);
    }
}
