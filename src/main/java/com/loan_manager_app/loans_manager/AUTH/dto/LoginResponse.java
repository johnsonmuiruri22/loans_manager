package com.loan_manager_app.loans_manager.AUTH.dto;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private String role;
    private String username;
    private Long userId;
    private String deviceName;
    private String ipAddress;
}
