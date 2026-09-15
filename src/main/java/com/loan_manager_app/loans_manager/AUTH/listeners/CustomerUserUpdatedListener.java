package com.loan_manager_app.loans_manager.AUTH.listeners;


import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.CUSTOMERS.events.UpdateCustomerUserEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUserUpdatedListener {
    private final UserRepository userRepository;


    @ApplicationModuleListener
    @Transactional
    public void onCustomerUserUpdated(UpdateCustomerUserEvent event){
        Users userToUpdate = userRepository.findByUsername(event.userEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
        userToUpdate.setSurname(event.surname());
        userToUpdate.setOtherNames(event.otherNames());
        userRepository.save(userToUpdate);
    }
}

