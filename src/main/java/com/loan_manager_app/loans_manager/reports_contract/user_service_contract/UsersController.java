package com.loan_manager_app.loans_manager.reports_contract.user_service_contract;


import com.loan_manager_app.loans_manager.AUTH.api.UserAccountLookupService;
import com.loan_manager_app.loans_manager.AUTH.api.UserCardResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan-management/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserAccountLookupService userAccountLookupService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UserCardResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(userAccountLookupService.getUserRecords(page, size));
    }
}
