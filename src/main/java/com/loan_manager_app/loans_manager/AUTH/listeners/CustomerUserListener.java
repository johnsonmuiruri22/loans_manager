package com.loan_manager_app.loans_manager.AUTH.listeners;


import com.loan_manager_app.loans_manager.AUTH.account.CustomerAccount;
import com.loan_manager_app.loans_manager.AUTH.account.CustomerAccountRepository;
import com.loan_manager_app.loans_manager.AUTH.api.UserCreatedEvent;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerCreatedEvent;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.Role;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerUserListener {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final CustomerAccountRepository accountRepository;

    @ApplicationModuleListener
    public void onCustomerRegistered(CustomerCreatedEvent event){
        String temporaryPassword = UUID.randomUUID()
                .toString()
                .substring(0,8);
        Users createCustomerUser = new Users();
        createCustomerUser.setSurname(event.customerSurname());
        createCustomerUser.setOtherNames(event.customerOtherNames());
        createCustomerUser.setUsername(event.customerEmail());
        createCustomerUser.setRole(Role.CUSTOMER);
        createCustomerUser.setStatus(Status.ACTIVE);

        System.out.println("TASK 1: Creating user for customer: "+event.customerEmail()+" ");
        System.out.println("TASK 2: Setting temporary password for customer: "+event.customerEmail()+" ");
        System.out.println("TASK 3: Setting mustChangePassword flag for customer: "+event.customerEmail()+" ");
        System.out.println("TASK 4: Temporary password before encoding: "+temporaryPassword+"");
        createCustomerUser.setPassword(passwordEncoder.encode(temporaryPassword));
        createCustomerUser.setMustChangePassword(true);
        Users savedCustomerUser = userRepository.save(createCustomerUser);

        accountRepository.save(
                new CustomerAccount(
                        event.customerId(),
                        savedCustomerUser.getUserId()
                )
        );

        eventPublisher.publishEvent(new UserCreatedEvent(
                event.customerEmail(),
                temporaryPassword,
                event.customerEmail()
        ));
    }
}
