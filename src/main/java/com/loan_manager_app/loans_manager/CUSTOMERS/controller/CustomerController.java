package com.loan_manager_app.loans_manager.CUSTOMERS.controller;


import com.loan_manager_app.loans_manager.CUSTOMERS.DTOS.CustomerReqDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.service.CustomerService;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PostMapping("/register")
    public ResponseEntity<AppResponse> registerCustomer(@RequestBody CustomerReqDTO customerReqDTO){
        return ResponseEntity.ok(customerService.registerCustomer(customerReqDTO));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/get/all")
    public ResponseEntity<PageResponse<CustomerRespDTO>> getCustomer(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<AppResponse> deleteCustomer(@PathVariable Long customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(AppResponse.builder().message("Customer deleted successfully").build());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerRespDTO> updateCustomer(@PathVariable Long customerId,
                                                      @RequestBody CustomerReqDTO customerReqDTO){
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerReqDTO));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/get/customer/{customerId}")
    public ResponseEntity<AppResponse> getCustomerById(@PathVariable Long customerId){
        return ResponseEntity.ok(AppResponse.builder()
                .customerToGet(customerService.getCustomerById(customerId)).build());
    }
}
