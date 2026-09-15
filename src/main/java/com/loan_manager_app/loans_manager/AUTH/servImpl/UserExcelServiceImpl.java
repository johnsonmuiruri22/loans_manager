package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUTH.api.UserCardResponse;
import com.loan_manager_app.loans_manager.AUTH.api.UserExcelService;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.SHARED.exports.utils.ExcelWorkbookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExcelServiceImpl implements UserExcelService {
    private final UserRepository userRepository;

    @Override
    public byte[] exportUsersToExcel() {
        List<Users> users = userRepository.findAll();

        List<UserCardResponse> usersToExport = users
                .stream()
                .map(
                        user -> UserCardResponse.builder()
                                .userId(user.getUserId())
                                .surname(user.getSurname())
                                .otherNames(user.getOtherNames())
                                .username(user.getUsername())
                                .userRole(user.getRole().name().toLowerCase())
                                .build()
                )
                .toList();

        String[] headers = {"User ID", "Surname", "Other Names", "Username", "User Role"};

        List<Object[]> rows = usersToExport
                .stream()
                .map(
                        userToExport-> new Object[]{
                                userToExport.userId(),
                                userToExport.surname(),
                                userToExport.otherNames(),
                                userToExport.username(),
                                userToExport.userRole()
                        }
                ).toList();

        return ExcelWorkbookUtil.createWorkbook(
                "users",
                headers,
                rows
        );
    }
}
