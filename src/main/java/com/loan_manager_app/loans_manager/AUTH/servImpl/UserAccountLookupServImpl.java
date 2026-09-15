package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUTH.api.UserCardResponse;
import com.loan_manager_app.loans_manager.AUTH.api.UserAccountLookupService;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountLookupServImpl implements UserAccountLookupService {
    private final UserRepository userRepository;

    @Override
    public PageResponse<UserCardResponse> getUserRecords(int page, int size) {
        Page<Users> usersPage =
                userRepository.findAllBy(PageRequest.of(page, size));

        List<UserCardResponse> userCardResponseList = usersPage
                        .getContent()
                        .stream()
                .map(user -> UserCardResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .surname(user.getSurname())
                        .otherNames(user.getOtherNames())
                        .userRole(user.getRole().name().toLowerCase())
                        .build())
                .toList();
        return PageResponse.<UserCardResponse>builder()
                .content(userCardResponseList)
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .size(usersPage.getSize())
                .first(usersPage.isFirst())
                .last(usersPage.isLast())
                .build();
    }
}
