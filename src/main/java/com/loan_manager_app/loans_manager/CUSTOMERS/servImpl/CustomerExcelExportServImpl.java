package com.loan_manager_app.loans_manager.CUSTOMERS.servImpl;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import com.loan_manager_app.loans_manager.CUSTOMERS.repo.CustomerRepository;
import com.loan_manager_app.loans_manager.SHARED.exports.service.CustomerExcelExportService;
import com.loan_manager_app.loans_manager.SHARED.exports.utils.ExcelWorkbookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerExcelExportServImpl implements CustomerExcelExportService {
    private final CustomerRepository customerRepository;

    @Override
    public byte[] exportCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerRespDTO> customerList = customers
                .stream()
                .map(
                        customer -> CustomerRespDTO.builder()
                                .customerId(customer.getCustomerId())
                                .surname(customer.getSurname())
                                .otherNames(customer.getOtherNames())
                                .gender(customer.getGender())
                                .dob(customer.getDob())
                                .monthlyIncome(customer.getMonthlyIncome())
                                .residence(customer.getResidence())
                                .phone(customer.getPhone())
                                .email(customer.getEmail())
                                .creditStatus(customer.getCreditStatus().toString())
                                .loanCount(customer.getLoanCount())
                                .build()
                )
                .toList();

        String[] headers = {
                "Customer ID",
                "Surname",
                "Other Names",
                "Gender",
                "Date of Birth",
                "Monthly Income",
                "Residence",
                "Phone",
                "Email",
                "Credit Status",
                "Loan Count"
        };

        List<Object[]> rows = customerList.stream()
                .map(customer -> new Object[]{

                        customer.getCustomerId(),
                        customer.getSurname(),
                        customer.getOtherNames(),
                        customer.getGender(),
                        customer.getDob(),
                        customer.getMonthlyIncome(),
                        customer.getResidence(),
                        customer.getPhone(),
                        customer.getEmail(),
                        customer.getCreditStatus(),
                        customer.getLoanCount()

                })
                .toList();

        return ExcelWorkbookUtil.createWorkbook(
                "Customers",
                headers,
                rows
        );
    }
}
