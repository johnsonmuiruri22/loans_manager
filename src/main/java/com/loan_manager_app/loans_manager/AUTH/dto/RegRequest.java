package com.loan_manager_app.loans_manager.AUTH.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegRequest {
    private String surname;
    private String otherNames;
    private String username;
    private String password;
}
