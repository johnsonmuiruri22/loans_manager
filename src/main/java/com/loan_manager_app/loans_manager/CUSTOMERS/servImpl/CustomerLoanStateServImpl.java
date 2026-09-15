package com.loan_manager_app.loans_manager.CUSTOMERS.servImpl;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLoanStateService;
import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import com.loan_manager_app.loans_manager.CUSTOMERS.repo.CustomerRepository;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerLoanStateServImpl implements
        CustomerLoanStateService {
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void markLoanRequested(Long customerId) {
        Customer customer = getCustomer(customerId);
        customer.setHasRequestedLoan(true);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void markLoanRepaid(Long customerId) {
        Customer customerToGet = getCustomer(customerId);
        customerToGet.setHasRepaidLoan(true);
        customerRepository.save(customerToGet);
    }

    @Override
    @Transactional
    public void incrementLoanCount(Long customerId) {
        Customer customer = getCustomer(customerId);
        customer.setLoanCount(customer.getLoanCount() + 1);
        customerRepository.save(customer);
    }

    private Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    private Customer getCustomer(Long customerId) {
        return getCustomerById(customerId).
                orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
    }
}
