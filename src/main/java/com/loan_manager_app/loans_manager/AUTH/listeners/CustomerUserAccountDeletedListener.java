package com.loan_manager_app.loans_manager.AUTH.listeners;


import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerDeletedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUserAccountDeletedListener {
    private final UserRepository userRepository;

    @ApplicationModuleListener
    @Transactional
    public void onCustomerAccountDeleted(CustomerDeletedEvent customerDeletedEvent){
        Users customerUserToDelete =
                userRepository.findByUsername(customerDeletedEvent.customerUsername())
                        .orElseThrow(()->new RuntimeException("Customer user not found"));
        userRepository.delete(customerUserToDelete);
    }
}

