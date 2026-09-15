package com.loan_manager_app.loans_manager.SHARED.appResponse;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ApiErrorResponse {
    private String message;
    private String error;
    private int statusCode;
    private LocalDateTime timestamp;
    private String path;
}
