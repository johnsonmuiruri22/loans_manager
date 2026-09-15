package com.loan_manager_app.loans_manager.CUSTOMERS.service;

import com.loan_manager_app.loans_manager.CUSTOMERS.DTOS.CustomerReqDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;

public interface CustomerService {
    AppResponse registerCustomer(CustomerReqDTO customerReqDTO);
    CustomerRespDTO updateCustomer(Long customerId, CustomerReqDTO customerReqDTO);
    PageResponse<CustomerRespDTO> getAllCustomers(int page, int size);
    void deleteCustomer(Long customerId);
    CustomerRespDTO getCustomerById(Long customerId);
}
