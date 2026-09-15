package com.loan_manager_app.loans_manager.CUSTOMERS.servImpl;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.DTOS.CustomerReqDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.Mapper.CustomerMapper;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerCreatedEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerDeletedEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.events.CustomerRegisteredEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.events.UpdateCustomerUserEvent;
import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import com.loan_manager_app.loans_manager.CUSTOMERS.repo.CustomerRepository;
import com.loan_manager_app.loans_manager.CUSTOMERS.service.CustomerService;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.CreditStatus;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.CustomerNotFoundException;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfoExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceInfoExtractor deviceInfoExtractor;


    @Transactional
    @Override
    public AppResponse registerCustomer(CustomerReqDTO customerReqDTO) {
        boolean customerExists = customerRepository.existsByEmailAndPhone(customerReqDTO.getEmail(),
                customerReqDTO.getPhone());
        if (customerExists){
            return AppResponse.builder().message("Customer already exists").statusCode(409).build();
        }

        Customer newCustomer = Customer.builder()
                .surname(customerReqDTO.getSurname())
                .otherNames(customerReqDTO.getOtherNames())
                .email(customerReqDTO.getEmail())
                .phone(customerReqDTO.getPhone())
                .gender(customerReqDTO.getGender())
                .dob(customerReqDTO.getDob())
                .occupation(customerReqDTO.getOccupation())
                .monthlyIncome(customerReqDTO.getMonthlyIncome())
                .residence(customerReqDTO.getResidence())
                .creditStatus(CreditStatus.GOOD)
                .hasRequestedLoan(false)
                .hasRepaidLoan(false)
                .loanCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(newCustomer);

        eventPublisher.publishEvent(new CustomerCreatedEvent(
                savedCustomer.getCustomerId(),
                savedCustomer.getSurname(),
                savedCustomer.getOtherNames(),
                savedCustomer.getEmail()
        ));

        eventPublisher.publishEvent(
                new CustomerRegisteredEvent(
                        savedCustomer.getCustomerId(),
                        savedCustomer.getSurname()+" "+savedCustomer.getOtherNames(),
                        savedCustomer.getEmail()
                )
        );

        CustomerRespDTO customerRespDTO = CustomerMapper.mapToCustomerRespDTO(savedCustomer);

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();

        eventPublisher.publishEvent(
                new RegisterLogEvent("A new customer record has been created: "+customerRespDTO.getSurname()
                        +" "+customerRespDTO.getOtherNames(),
                        LogType.CUSTOMER_CREATED, deviceInfo)
        );
        return AppResponse.builder()
                .message("Customer registered successfully")
                .statusCode(201)
                .customerToGet(customerRespDTO)
                .build();
    }

    @Transactional
    @Override
    public CustomerRespDTO updateCustomer(Long customerId, CustomerReqDTO customerReqDTO) {
        Customer customerToUpdate = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerToUpdate.setSurname(customerReqDTO.getSurname());
        customerToUpdate.setOtherNames(customerReqDTO.getOtherNames());
        customerToUpdate.setEmail(customerReqDTO.getEmail());
        customerToUpdate.setPhone(customerReqDTO.getPhone());
        customerToUpdate.setGender(customerReqDTO.getGender());
        customerToUpdate.setDob(customerReqDTO.getDob());
        customerToUpdate.setOccupation(customerReqDTO.getOccupation());
        customerToUpdate.setMonthlyIncome(customerReqDTO.getMonthlyIncome());
        customerToUpdate.setResidence(customerReqDTO.getResidence());
        customerToUpdate.setUpdatedAt(LocalDateTime.now());
        customerToUpdate.setCreditStatus(CreditStatus.GOOD);
        Customer updatedCustomer =  customerRepository.save(customerToUpdate);

        eventPublisher.publishEvent(
                new UpdateCustomerUserEvent(
                        customerReqDTO.getSurname(),
                        customerReqDTO.getOtherNames(),
                        customerReqDTO.getEmail()
                )
        );

        CustomerRespDTO customerRespDTO = CustomerMapper.mapToCustomerRespDTO(updatedCustomer);
        customerRespDTO.setCustomMessage("Customer updated successfully");
        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "Updated customer record",
                        LogType.CUSTOMER_UPDATED, deviceInfo
                )
        );
        return customerRespDTO;
    }


    @Override
    public PageResponse<CustomerRespDTO> getAllCustomers(int page, int size) {
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page, size));
        List<CustomerRespDTO> customerList = customers.getContent()
                .stream()
                .map(CustomerMapper::mapToCustomerRespDTO)
                .toList();

        return PageResponse.<CustomerRespDTO>builder()
                .content(customerList)
                .totalPages(customers.getTotalPages())
                .totalElements(customers.getTotalElements())
                .page(customers.getNumber())
                .first(customers.isFirst())
                .last(customers.isLast())
                .totalPages(customers.getTotalPages())
                .build();

    }

    @Transactional
    @Override
    public void deleteCustomer(Long customerId) {
        Customer customerToDelete = customerRepository
                .findByCustomerId(customerId)
                        .orElseThrow(()->new CustomerNotFoundException("customer not found!"));
        if (customerToDelete.isHasRequestedLoan() || !customerToDelete.isHasRepaidLoan()){
            throw new CustomerNotFoundException("Customer has applied for loan and cannot be deleted!");
        }

        eventPublisher.publishEvent(
                new CustomerDeletedEvent(customerToDelete.getEmail())
        );

        customerRepository.delete(customerToDelete);
        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent(
                        "customer record has been deleted",
                        LogType.CUSTOMER_DELETED,
                        deviceInfo
                )
        );
    }

    @Override
    public CustomerRespDTO getCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        return customer.map(CustomerMapper::mapToCustomerRespDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

}
