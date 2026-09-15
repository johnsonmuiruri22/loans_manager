package com.loan_manager_app.loans_manager.AUTH.api;

import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;

public interface UserAccountLookupService {
    PageResponse<UserCardResponse> getUserRecords(int page, int size);
}
